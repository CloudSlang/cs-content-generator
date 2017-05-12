package io.cloudslang.tools.generator.utils;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;

import static org.apache.commons.io.FilenameUtils.getBaseName;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.split;

/**
 * Author: Ligia Centea
 * Date: 4/12/2016.
 */
public class PackageUtils {

    public static final String JAVA = "java";
    public static final String CLASSES = "classes";

    public static String getCommonPackage(String first, String second) {
        if (first == null || second == null) {
            return null;
        }

        String commonPackage = "";
        Iterator<String> firstIterator = Arrays.stream(split(first, ".")).iterator();
        Iterator<String> secondIterator = Arrays.stream(split(second, ".")).iterator();
        boolean stillEqual = true;

        while (stillEqual && firstIterator.hasNext() && secondIterator.hasNext()) {
            String nextName = firstIterator.next();
            if (nextName.equals(secondIterator.next())) {
                if (!isEmpty(commonPackage)) {
                    commonPackage += ".";
                }
                commonPackage += nextName;
            } else {
                stillEqual = false;
            }
        }
        return commonPackage;
    }

    public static String getPackageFromPath(Path path) {
        String packageName = "";
        for (Path aPath : path) {
            String name = getBaseName(aPath.toString());
            if (JAVA.equalsIgnoreCase(name) || CLASSES.equalsIgnoreCase(name)) {
                packageName = "";
            } else {
                if (!isEmpty(packageName)) {
                    packageName += ".";
                }
                packageName += name;

            }
        }
        return packageName;
    }
}
