package io.cloudslang.tools.generator.entities;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class CsInput implements Mappable {

    private final String name;
    private final String description;
    private final boolean required;
    private final String defaultValue;
    private final boolean privateField;
    private final boolean sensitive;

    public CsInput(String name, String description, boolean required, String defaultValue, boolean sensitive, boolean overridable) {
        this.name = name;
        this.description = description;
        this.required = required;
        this.defaultValue = StringUtils.defaultIfEmpty(defaultValue, "");
        this.sensitive = sensitive;
        this.privateField = overridable;
    }

    public Map<String, Object> toMap() {
        boolean hasDefault = StringUtils.isNotEmpty(defaultValue);
        final Map<String, Object> csInputMap = new HashMap<>();
        csInputMap.put("name", name);
        csInputMap.put("description", description);
        csInputMap.put("default", defaultValue);
        csInputMap.put("hasAny", hasDefault || !required || sensitive);
        csInputMap.put("hasDefault", hasDefault);
        csInputMap.put("isRequired", required);
        csInputMap.put("isPrivate", privateField);
        csInputMap.put("isSensitive", sensitive);
        return csInputMap;

    }

    public String getDescription() { return description; }
    public String getName() {
        return name;
    }
    public boolean isRequired() {
        return required;
    }
    public boolean isPrivateField() {
        return privateField;
    }


}
