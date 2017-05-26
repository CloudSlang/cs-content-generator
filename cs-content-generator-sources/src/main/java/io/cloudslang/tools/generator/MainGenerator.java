package io.cloudslang.tools.generator;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MainGenerator {
    public static final Path jarPath = Paths.get("c:\\users\\moldovso\\desktop\\testGenerator\\cs-http-client-0.1.71.jar");
    public static final String className = "";
    public static final Path destination = Paths.get("c:\\users\\moldovso\\desktop\\tempcs");

    public static void main(String[] args) {
        CsGenerator csGenerator = new CsGenerator();
        csGenerator.generateWrapper(jarPath, className, destination);
    }
}