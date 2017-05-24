package io.cloudslang.tools.generator;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static final Path jarPath = Paths.get("c:\\users\\moldovso\\desktop\\cs-amazon-1.0.10-SNAPSHOT.jar");
    public static final String className = "";
    public static final Path destination = Paths.get("c:\\users\\moldovso\\desktop\\tempcs");

    public static void main(String[] args) {
        CsGenerator csGenerator = new CsGenerator();
        csGenerator.generateWrapper(jarPath, className, destination);
    }
}