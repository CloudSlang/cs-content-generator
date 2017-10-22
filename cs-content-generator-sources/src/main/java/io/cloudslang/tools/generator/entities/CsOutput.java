package io.cloudslang.tools.generator.entities;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class CsOutput  implements Mappable{

    private final String name;
    private final String description;
    private final String expression;

    public CsOutput(String name, String description, String expression) {
        this.name = name;
        this.description = description;
        this.expression = StringUtils.defaultIfEmpty(expression, "");
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> csOutputMap = new HashMap<>(3);
        csOutputMap.put("name", name);
        csOutputMap.put("description", description);
        csOutputMap.put("hasValue", StringUtils.isNoneEmpty(expression));
        csOutputMap.put("value", expression);
        return csOutputMap;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
