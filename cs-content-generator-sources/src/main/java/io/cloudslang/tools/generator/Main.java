package io.cloudslang.tools.generator;

/**
 * Created by moldovso on 5/23/2017.
 */
public class Main {
    public static final String jarPath = "c:\\users\\moldovso\\desktop\\cs-amazon-1.0.10-SNAPSHOT.jar";
    public static final String className = "";
//    public static final String className = "io.cloudslang.base.http";
    public static final String destination = "c:\\users\\moldovso\\desktop\\tempcs";

    public static void main(String[] args) {
        CsGenerator csGenerator = new CsGenerator();
        csGenerator.generateWrapper(jarPath, className, destination);
    }
}
