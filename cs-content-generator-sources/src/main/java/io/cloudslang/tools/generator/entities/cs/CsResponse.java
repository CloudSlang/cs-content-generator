package io.cloudslang.tools.generator.entities.cs;

/**
 * Author: Ligia Centea
 * Date: 4/3/2016.
 */
public class CsResponse {

    private String name;
    private String rule;

    public CsResponse(String name, String rule) {
        this.name = name;
        this.rule = rule;
    }

    public CsResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
