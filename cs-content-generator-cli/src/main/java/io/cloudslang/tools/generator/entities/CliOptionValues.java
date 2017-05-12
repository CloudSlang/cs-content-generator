package io.cloudslang.tools.generator.entities;

/**
 * Author: Ligia Centea
 * Date: 4/18/2016.
 */
public class CliOptionValues {

    private boolean help;
    private boolean generateCloudSlang;
    private String javaSource;
    private String destination;
    private String classFilter;

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isGenerateCloudSlang() {
        return generateCloudSlang;
    }

    public void setGenerateCloudSlang(boolean generateCloudSlang) {
        this.generateCloudSlang = generateCloudSlang;
    }

    public String getJavaSource() {
        return javaSource;
    }

    public void setJavaSource(String javaSource) {
        this.javaSource = javaSource;
    }

    public String getClassFilter() {
        return classFilter;
    }

    public void setClassFilter(String classFilter) {
        this.classFilter = classFilter;
    }
}
