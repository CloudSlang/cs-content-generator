package io.cloudslang.tools.generator.entities.cs;

import io.cloudslang.tools.generator.entities.annotations.Property;

/**
 * Author: Ligia Centea
 * Date: 4/3/2016.
 */
public class CsJavaAction {

    private String gav;

    @Property(key = "class_name")
    private String methodName;

    @Property(key = "method_name")
    private String className;

    public CsJavaAction() {
    }

    public CsJavaAction(String gav, String methodName, String className) {
        this.gav = gav;
        this.className = methodName;
        this.methodName = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGav() {
        return gav;
    }

    public void setGav(String gav) {
        this.gav = gav;
    }
}
