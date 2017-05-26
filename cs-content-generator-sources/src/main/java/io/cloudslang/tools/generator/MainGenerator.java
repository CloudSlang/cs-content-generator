package io.cloudslang.tools.generator;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MainGenerator {
    public static final Path jarPath = Paths.get("c:\\users\\moldovso\\desktop\\testGenerator\\cs-amazon-1.0.10-SNAPSHOT.jar");
    public static final String className = "";
    public static final Path destination = Paths.get("c:\\users\\moldovso\\cloudslang\\cloud-slang-content/content/io/cloudslang/amazon");

    public static void main(String[] args) {
        CsGenerator csGenerator = new CsGenerator();
        csGenerator.generateWrapper(jarPath, className, destination);
    }
}