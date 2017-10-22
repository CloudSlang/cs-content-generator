package io.cloudslang.tools.generator;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MainGenerator {
    public static final Path jarPath = Paths.get(System.getProperty("user.home") + "\\Desktop\\cs-azure-0.0.6-SNAPSHOT.jar");
    public static final String className = "";
    public static final Path destination = Paths.get(System.getProperty("user.home") + "\\Desktop\\potato");

    public static void main(String[] args) {
        CsGenerator csGenerator = new CsGenerator();
        csGenerator.generateWrapper(jarPath, className, destination);
    }
}