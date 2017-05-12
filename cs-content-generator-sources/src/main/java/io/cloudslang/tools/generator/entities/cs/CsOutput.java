package io.cloudslang.tools.generator.entities.cs;

/**
 * Author: Ligia Centea
 * Date: 4/3/2016.
 */
public class CsOutput {

    private String name;
    private String expression;

    public CsOutput() {
    }

    public CsOutput(String name, String expression) {
        this.name = name;
        this.expression = expression;
    }

    public CsOutput(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
