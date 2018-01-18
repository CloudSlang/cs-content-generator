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

    @Parameter(property = "jarFile", required = true)
    protected File jarFile;

    @Parameter(property = "className")
    protected String className;

    @Parameter(property = "destination", required = true)
    protected File destination;

    public void execute() throws MojoExecutionException {

        try {
            CsGenerator csGenerator = new CsGenerator();
            csGenerator.generateWrapper(jarFile.toPath(), className, destination.toPath());
            getLog().info("Generating content for Java Package" + jarFile.getName());
        } catch (Exception e) {
            throw new MojoExecutionException("Error creating content for Java Package" + jarFile.getName(), e);
        }

}

}
