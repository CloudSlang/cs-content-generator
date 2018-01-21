/*
 * (c) Copyright 2017 Micro Focus, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudslang.tools;

import io.cloudslang.tools.generator.CsGenerator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo(name = "generate",
        defaultPhase = LifecyclePhase.PACKAGE)

public class Generate extends AbstractMojo {

    @Parameter(property = "jarFile", defaultValue = "${project.build.directory}/${project.artifactId}-${project.version}.jar")
    protected File jarFile;

    @Parameter(property = "className")
    protected String className;

    @Parameter(property = "destination", defaultValue = "${project.build.directory}/generated-slang")
    protected File destination;

    public void execute() throws MojoExecutionException {

        try {
            CsGenerator csGenerator = new CsGenerator();
            csGenerator.generateWrapper(jarFile.toPath(), className, destination.toPath());
            getLog().info("Generating content for Java Package: " + jarFile.getAbsolutePath());
        } catch (Exception e) {
            throw new MojoExecutionException("Error creating content for Java Package: " + jarFile.getName(), e);
        }

}

}
