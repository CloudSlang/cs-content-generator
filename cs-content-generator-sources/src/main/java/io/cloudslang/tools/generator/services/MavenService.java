/*
 * Copyright © 2017 EntIT Software LLC, a Micro Focus company (L.P.)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
