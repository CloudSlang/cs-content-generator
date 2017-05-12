package io.cloudslang.tools.generator.services.fs;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.WriterFactory;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

import static org.springframework.util.Assert.notNull;

/**
 * Author: Ligia Centea
 * Date: 3/25/2016.
 */
@Service
public class MavenService {

    @Autowired
    private MavenXpp3Writer mavenXpp3Writer;

    @Autowired
    private MavenXpp3Reader mavenXpp3Reader;

    @Autowired
    private JarService jarService;

    public void createMavenProject(Path destination, String group, String artifact, String version) throws IOException, ModelBuildingException, XmlPullParserException, DocumentException {
        notNull(destination);
        //use fileSystem.getPath in order to easily inject a file system for testing
        Path pluginDest = destination.getFileSystem().getPath(destination.toString(), artifact);
        if (!Files.exists(pluginDest)) {
            Files.createDirectories(pluginDest);
        }
        createDirectoryIfNotExists(destination.getFileSystem().getPath(pluginDest.toString(), "src"));

        Path main = destination.getFileSystem().getPath(pluginDest.toString(), "src", "main");
        createDirectoryIfNotExists(main);
        createResourcesDirectories(main);

        Path test = destination.getFileSystem().getPath(pluginDest.toString(), "src", "test");
        createDirectoryIfNotExists(test);
        createResourcesDirectories(test);

        createPom(pluginDest, group, artifact, version);
    }

    private void createResourcesDirectories(Path destination) throws IOException {
        createDirectoryIfNotExists(destination.getFileSystem().getPath(destination.toString(), "java"));
        createDirectoryIfNotExists(destination.getFileSystem().getPath(destination.toString(), "resources"));
    }

    private void createPom(Path pluginDest, String group, String artifact, String version) throws IOException, ModelBuildingException, XmlPullParserException, DocumentException {
        Path parent = pluginDest.getParent();
        Model model = loadPom(new ClassPathResource("/services/pom.xml").getInputStream());
        model.setArtifactId(artifact);

        Path parentPom = pluginDest.getFileSystem().getPath(parent.toString(), "pom.xml");
        if (Files.exists(parentPom)) {
            Model parentModel = loadPom(parentPom);
            Parent parentDetails = convertModelToParent(parentModel);
            model.setParent(parentDetails);
            parentModel.addModule(model.getArtifactId());
            saveModel(parentModel, parentPom.toString());
        } else {
            model.setGroupId(group);
            model.setVersion(version);
        }
        saveModel(model, pluginDest.getFileSystem().getPath(pluginDest.toString(), "pom.xml").toString());
    }

    private void createDirectoryIfNotExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
    }

    public Model loadPom(Path file) throws IOException, XmlPullParserException, ModelBuildingException {
        return loadPom(new FileInputStream(file.toFile()));
    }

    public Model loadPom(InputStream inputStream) throws IOException, XmlPullParserException, ModelBuildingException {
        return mavenXpp3Reader.read(inputStream);
    }

    public String getGroup(Model m) {
        return getConfigurationFromParent(m, Model::getGroupId, Parent::getGroupId);
    }

    public String getArtifactId(Model m) {
        return getConfigurationFromParent(m, Model::getArtifactId, Parent::getArtifactId);
    }

    public String getVersion(Model m) {
        return getConfigurationFromParent(m, Model::getVersion, Parent::getVersion);
    }

    public String getProperty(Model model, String name) {
        notNull(model);
        return model.getProperties().getProperty(name);
    }

    public void addProperty(Model model, String name, String value) {
        notNull(model);
        model.addProperty(name, value);
    }

    public void addDependency(Model model, String groupId, String artifactId, String version) {
        notNull(model);
        if (!hasDependency(model.getDependencies(), groupId, artifactId)) {
            model.addDependency(getDependency(groupId, artifactId, version));
        }
    }

    private boolean hasDependency(List<Dependency> dependencies, String group, String artifactId) {
        return dependencies.stream().anyMatch(d -> d.getGroupId().equals(group)
                && d.getArtifactId().equals(artifactId));
    }

    public void addDependencyManagementEntry(Model model, String groupId, String artifactId, String version) {
        notNull(model);
        if (hasDependencyManagement(model)) {
            DependencyManagement dm = model.getDependencyManagement();
            if (!hasDependency(dm.getDependencies(), groupId, artifactId)) {
                dm.addDependency(getDependency(groupId, artifactId, version));
            }
        }
    }

    public boolean hasDependencyManagement(Model model) {
        return model != null && model.getDependencyManagement() != null;
    }

    private Dependency getDependency(String groupId, String artifactId, String version) {
        Dependency dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifactId);
        if (version != null) {
            dependency.setVersion(version);
        }
        return dependency;
    }

    public void saveModel(Model model, String path) throws IOException {
        try (Writer fileWriter = WriterFactory.newXmlWriter(new File(path))) {
            mavenXpp3Writer.write(fileWriter, model);
        }
    }

    private Parent convertModelToParent(Model model) {
        notNull(model);
        Parent parent = new Parent();
        parent.setGroupId(getGroup(model));
        parent.setArtifactId(getArtifactId(model));
        parent.setVersion(model.getVersion());
        return parent;
    }

    private String getConfigurationFromParent(Model model, Function<Model, String> getFromModel, Function<Parent, String> getFromParent) {
        notNull(model);
        String prop = getFromModel.apply(model);
        if (prop == null && model.getParent() != null) {
            return getFromParent.apply(model.getParent());
        }
        return prop;
    }

    public String findArtifactGav(Path file) throws IOException, ModelBuildingException, XmlPullParserException {
        Model model = loadPom(jarService.getPomFromJar(file));
        if (model != null) {
            return String.format("%s:%s:%s", getGroup(model), getArtifactId(model), getVersion(model));
        }
        throw new IllegalStateException("Could not read the POm from Jar: " + file);
    }
}
