package io.cloudslang.tools.generator.entities.cs;

import java.util.List;

/**
 * Author: Ligia Centea
 * Date: 5/16/2016.
 */
public class CsFlow {
    private String name;
    private List<CsInput> inputs;
    private List<CsTask> tasks;
    private List<CsOutput> outputs;
    private List<CsResponse> responses;

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

    public List<CsTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<CsTask> tasks) {
        this.tasks = tasks;
    }

    public List<CsOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<CsOutput> outputs) {
        this.outputs = outputs;
    }

    public List<CsResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<CsResponse> responses) {
        this.responses = responses;
    }
}
