package io.cloudslang.tools.generator.entities;

/**
 * Author: Ligia Centea
 * Date: 4/18/2016.
 */
public enum OptionsEnum {
    HELP("h", "help", null),
    GENERATE_CLOUDSLANG("cs", "cloudslang", "Create CloudSlang operations which calls Java classes from a specified jar"),
    SOURCES_PATH("s", "src", "The absolute path of the .jar file that contains @Actions for which CloudSlang operations will be generated"),
    FILTER("f", "filter", "The fully qualified name of the Java class to be filtered out from the specified jar file. " +
            "This is the only class for which a CloudSlang operation will be generated"),
    DESTINATION("d", "destination", "The absolute path of the new Maven module or of the CloudSlang sources");

    private String shortName;
    private String longName;
    private String description;

    OptionsEnum(String shortName, String longName, String description) {
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
