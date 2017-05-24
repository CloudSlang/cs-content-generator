package io.cloudslang.tools.generator.services;

import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Path;
import java.util.function.Function;

public class MavenService {

//    private static MavenXpp3Writer mavenXpp3Writer = new MavenXpp3Writer();

    private static MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();

//    private static void createDirectoryIfNotExists(Path path) throws IOException {
//        if (!Files.exists(path)) {
//            Files.createDirectory(path);
//        }
//    }
//
//    public static Model loadPom(Path file) throws IOException, XmlPullParserException, ModelBuildingException {
//        return loadPom(new FileInputStream(file.toFile()));
//    }

    public static Model loadPom(InputStream inputStream) throws IOException, XmlPullParserException, ModelBuildingException {
        return mavenXpp3Reader.read(inputStream);
    }

    public static String getGroup(Model m) {
        return getConfigurationFromParent(m, Model::getGroupId, Parent::getGroupId);
    }

    public static String getArtifactId(Model m) {
        return getConfigurationFromParent(m, Model::getArtifactId, Parent::getArtifactId);
    }

    public static String getVersion(Model m) {
        return getConfigurationFromParent(m, Model::getVersion, Parent::getVersion);
    }

//    public static String getProperty(@NotNull Model model, String name) {
//        return model.getProperties().getProperty(name);
//    }
//
//    public static void addProperty(@NotNull Model model, String name, String value) {
//        model.addProperty(name, value);
//    }

//    public static void addDependency(@NotNull Model model, String groupId, String artifactId, String version) {
//        if (!hasDependency(model.getDependencies(), groupId, artifactId)) {
//            model.addDependency(getDependency(groupId, artifactId, version));
//        }
//    }

//    private static boolean hasDependency(List<Dependency> dependencies, String group, String artifactId) {
//        return dependencies.stream().anyMatch(d -> d.getGroupId().equals(group)
//                && d.getArtifactId().equals(artifactId));
//    }

//    public static void addDependencyManagementEntry(@NotNull Model model, String groupId, String artifactId, String version) {
//        if (hasDependencyManagement(model)) {
//            DependencyManagement dm = model.getDependencyManagement();
//            if (!hasDependency(dm.getDependencies(), groupId, artifactId)) {
//                dm.addDependency(getDependency(groupId, artifactId, version));
//            }
//        }
//    }

//    public static boolean hasDependencyManagement(Model model) {
//        return model != null && model.getDependencyManagement() != null;
//    }
//
//    private static Dependency getDependency(String groupId, String artifactId, String version) {
//        Dependency dependency = new Dependency();
//        dependency.setGroupId(groupId);
//        dependency.setArtifactId(artifactId);
//        if (version != null) {
//            dependency.setVersion(version);
//        }
//        return dependency;
//    }

//    public static void saveModel(Model model, String path) throws IOException {
//        try (Writer fileWriter = WriterFactory.newXmlWriter(new File(path))) {
//            mavenXpp3Writer.write(fileWriter, model);
//        }
//    }

//    private static Parent convertModelToParent(@NotNull Model model) {
//        Parent parent = new Parent();
//        parent.setGroupId(getGroup(model));
//        parent.setArtifactId(getArtifactId(model));
//        parent.setVersion(model.getVersion());
//        return parent;
//    }

    private static String getConfigurationFromParent(@NotNull Model model, Function<Model, String> getFromModel, Function<Parent, String> getFromParent) {
        String prop = getFromModel.apply(model);
        if (prop == null && model.getParent() != null) {
            return getFromParent.apply(model.getParent());
        }
        return prop;
    }

    public static String findArtifactGav(Path file) throws IOException, ModelBuildingException, XmlPullParserException {
        Model model = loadPom(JarService.getPomFromJar(file));
        if (model != null) {
            return String.format("%s:%s:%s", getGroup(model), getArtifactId(model), getVersion(model));
        }
        throw new IllegalStateException("Could not read the POM from Jar: " + file);
    }
}
