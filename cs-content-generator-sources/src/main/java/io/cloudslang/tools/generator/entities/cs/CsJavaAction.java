package io.cloudslang.tools.generator.entities.cs;

import java.util.HashMap;
import java.util.Map;

public class CsJavaAction implements Mappable {

    private final String gav;
    private final String methodName;
    private final String className;

    public CsJavaAction(String gav, String methodName, String className) {
        this.gav = gav;
        this.className = methodName;
        this.methodName = className;
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> javaActionMap = new HashMap<>();
        javaActionMap.put("gav", gav);
        javaActionMap.put("class_name", className);
        javaActionMap.put("method_name", methodName);
        return javaActionMap;
    }
}
