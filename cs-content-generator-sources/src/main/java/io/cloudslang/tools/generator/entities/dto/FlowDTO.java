package io.cloudslang.tools.generator.entities.dto;

import java.util.List;
import java.util.Map;

/**
 * Author: Ligia Centea
 * Date: 5/17/2016.
 */
public class FlowDTO {
    private String name;
    private List<Map<String, Map<String, Object>>> inputs;
    private List<Object> outputs;
    private List<Map<String, String>> workflow;
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

    public List<Map<String, String>> getWorkflow() {
        return workflow;
    }

    public void setWorkflow(List<Map<String, String>> workflow) {
        this.workflow = workflow;
    }
}
