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
