package io.cloudslang.tools.generator;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MainGenerator {
    public static final Path jarPath = Paths.get(System.getProperty("user.home") + "\\Desktop\\misc\\source\\cs-amazon-1.0.10-SNAPSHOT.jar");
    public static final String className = "";
    public static final Path destination = Paths.get(System.getProperty("user.home") + "\\cloudslang\\cloud-slang-content\\content");

    public static void main(String[] args) {
        CsGenerator csGenerator = new CsGenerator();
        csGenerator.generateWrapper(jarPath, className, destination);
    }
}