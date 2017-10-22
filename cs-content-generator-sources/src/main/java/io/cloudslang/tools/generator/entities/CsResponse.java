package io.cloudslang.tools.generator.entities;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class CsResponse implements Mappable{

    private String name;
    private String description;
    private String rule;

    public CsResponse(String name, String description, String rule) {
        this.name = name;
        this.description = description;
        this.rule = StringUtils.defaultIfEmpty(rule, "");
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> csResponseMap = new HashMap<>();
        csResponseMap.put("name", name);
        csResponseMap.put("description", description);
        csResponseMap.put("hasValue", StringUtils.isNoneEmpty(rule) && !name.equalsIgnoreCase("FAILURE")); //at the moment FAILURE should not have an evaluator
        csResponseMap.put("value", rule);
        return csResponseMap;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
