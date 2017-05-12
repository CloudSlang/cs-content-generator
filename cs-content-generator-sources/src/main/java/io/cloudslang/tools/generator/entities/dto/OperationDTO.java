package io.cloudslang.tools.generator.entities.dto;

import java.util.List;
import java.util.Map;

/**
 * Author: Ligia Centea
 * Date: 4/5/2016.
 */
public class OperationDTO {

    private String name;
    private List<Map<String, Map<String, Object>>> inputs;
    private Map<String, Object> java_action;
    private List<Object> outputs;
    private List<Object> results;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, Map<String, Object>>> getInputs() {
        return inputs;
    }

    public void setInputs(List<Map<String, Map<String, Object>>> inputs) {
        this.inputs = inputs;
    }

    public List<Object> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Object> outputs) {
        this.outputs = outputs;
    }

    public List<Object> getResults() {
        return results;
    }

    public void setResults(List<Object> results) {
        this.results = results;
    }

    public Map<String, Object> getJava_action() {
        return java_action;
    }

    public void setJava_action(Map<String, Object> java_action) {
        this.java_action = java_action;
    }
}
