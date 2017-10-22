package io.cloudslang.tools;


import io.cloudslang.tools.generator.CsGenerator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.nio.file.Path;

@Mojo(name = "generate",
        defaultPhase = LifecyclePhase.PACKAGE)

public class Generate extends AbstractMojo {

    @Parameter(property = "jarPath", required = true)
    protected Path jarPath;

    @Parameter(property = "className")
    protected String className;

    @Parameter(property = "destination", required = true)
    protected Path destination;

    public void execute() throws MojoExecutionException {

        try {
            CsGenerator csGenerator = new CsGenerator();
            csGenerator.generateWrapper(jarPath, className, destination);
            getLog().info("Generating content for Java Package" + jarPath.getFileName());
        } catch (Exception e) {
            throw new MojoExecutionException("Error creating content for Java Package" + jarPath.getFileName(), e);
        }

}

}
