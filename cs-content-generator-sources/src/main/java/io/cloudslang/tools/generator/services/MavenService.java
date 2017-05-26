package io.cloudslang.tools.generator.services;

import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.WriterFactory;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.dom4j.DocumentException;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

public class MavenService {

    public static Model pomToModel(String pomUrl) throws Exception {
        URL url = new URL(pomUrl);
        try (InputStream stream = url.openStream()) {
            return new MavenXpp3Reader().read(stream);
        }
    }

    private Parent convertModelToParent(@NotNull Model model) {
        Parent parent = new Parent();
        parent.setGroupId(getGroup(model));
        parent.setArtifactId(getArtifactId(model));
        parent.setVersion(model.getVersion());
        return parent;
    }

//    private void createPom(Path pluginDest, String group, String artifact, String version) throws IOException, ModelBuildingException, XmlPullParserException, DocumentException {
//        Path parent = pluginDest.getParent();
//        Model model = loadPom(new ClassPathResource("/services/pom.xml").getInputStream());
//        model.setArtifactId(artifact);

//        Path parentPom = pluginDest.getFileSystem().getPath(parent.toString(), "pom.xml");
//        if (Files.exists(parentPom)) {
//            Model parentModel = loadPom(parentPom);
//            Parent parentDetails = convertModelToParent(parentModel);
//            model.setParent(parentDetails);
//            parentModel.addModule(model.getArtifactId());
//            saveModel(parentModel, parentPom.toString());
//        } else {
//            model.setGroupId(group);
//            model.setVersion(version);
//        }
//        saveModel(model, pluginDest.getFileSystem().getPath(pluginDest.toString(), "pom.xml").toString());
//
//
//    }

    private static MavenXpp3Writer mavenXpp3Writer = new MavenXpp3Writer();

    public void saveModel(Model model, String path) throws IOException {
        try (Writer fileWriter = WriterFactory.newXmlWriter(new File(path))) {
            mavenXpp3Writer.write(fileWriter, model);
        }
    }

    private static MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();

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
