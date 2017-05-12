package io.cloudslang.tools.generator.entities.cs;

import java.util.List;

/**
 * Author: Ligia Centea
 * Date: 4/3/2016.
 */
public class CsOperation {

    private String name;
    private List<CsInput> inputs;
    private CsJavaAction action;
    private List<CsOutput> outputs;
    private List<CsResponse> results;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CsInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<CsInput> inputs) {
        this.inputs = inputs;
    }

    public CsJavaAction getAction() {
        return action;
    }

    public void setAction(CsJavaAction action) {
        this.action = action;
    }

    public List<CsOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<CsOutput> outputs) {
        this.outputs = outputs;
    }

    public List<CsResponse> getResults() {
        return results;
    }

    public void setResults(List<CsResponse> results) {
        this.results = results;
    }
}
