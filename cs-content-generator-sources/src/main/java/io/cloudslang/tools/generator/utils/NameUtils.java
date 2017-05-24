package io.cloudslang.tools.generator.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class NameUtils {

    private static final String CAMEL_SPLIT_REGEX = "(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])" +
            "|(?<=[a-zA-Z])(?=[0-9])|(?<=[0-9])(?=[a-zA-Z])";
    private static final List<String> RESERVED_WORDS = Arrays.asList("true", "false");
    private static final String RESULT_SUFFIX = "_RESULT";

    public static String getFullyQualifiedName(String packageName, String className) {
        if (packageName == null && className == null) {
            return null;
        } else if (packageName == null) {
            return className;
        } else if (className == null) {
            return packageName;
        } else {
            return packageName + "." + className;
        }
    }

    public static String getPackage(@NotNull String first, String... more) {
        StringBuilder packageName = new StringBuilder(first);

        for (String element : more) {
            if (!StringUtils.isEmpty(element) && !element.startsWith(".")) {
                packageName.append(".").append(element);
            } else if (!StringUtils.isEmpty(element) && element.startsWith(".")) {
                packageName.append(element);
            }
        }
        return packageName.toString();
    }

    public static String getActionName(@NotNull String value) {
        // TODO see if pattern can be used
        return Arrays.stream(value.split(CAMEL_SPLIT_REGEX))
                .collect(Collectors.joining(" "));
    }

    public static String toSnakeCase(@NotNull String value) {
        // TODO see if pattern can be used
        return Arrays.stream(value.split(CAMEL_SPLIT_REGEX))
                .map(StringUtils::lowerCase)
                .collect(Collectors.joining("_"));
    }

    public static String getResultName(@NotNull String value) {
        String result = StringUtils.replace(value, " ", "_");
        if (RESERVED_WORDS.contains(result.toLowerCase())) {
            result += RESULT_SUFFIX;
        }
        return StringUtils.upperCase(result);
    }
}
