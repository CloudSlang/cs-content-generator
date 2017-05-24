package io.cloudslang.tools.generator;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MainGenerator {
    public static final Path jarPath = Paths.get("c:\\users\\war\\desktop\\content\\cs-couchbase-0.0.1-SNAPSHOT.jar");
    public static final String className = "";
    public static final Path destination = Paths.get("c:\\users\\war\\desktop\\tempcs");

    public static void main(String[] args) {
        CsGenerator csGenerator = new CsGenerator();
        csGenerator.generateWrapper(jarPath, className, destination);
    }
}