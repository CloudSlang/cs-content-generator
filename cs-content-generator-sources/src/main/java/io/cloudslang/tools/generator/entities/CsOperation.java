package io.cloudslang.tools.generator.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CsOperation implements Mappable {

    private String name;
    private List<CsInput> inputs;
    private List<CsOutput> outputs;
    private CsJavaAction action;
    private List<CsResponse> results;

    public CsOperation(String name, List<CsInput> inputs, List<CsOutput> outputs, CsJavaAction action, List<CsResponse> results) {
        this.name = name;
        this.inputs = inputs;
        this.outputs = outputs;
        this.action = action;
        this.results = results;
    }

    public String getName() {
        return name;
    }
    public List<CsInput> getInputs() {
        return inputs;
    }
    public List<CsOutput> getOutputs() {
        return outputs;
    }
    public List<CsResponse> getResults() {
        return results;
    }


    @Override
    public Map<String, Object> toMap() {
        final Map<String, Object> operationMap = new HashMap<>();
        inputs.stream().map(Mappable::toMap).collect(Collectors.toList());
        operationMap.put("name", name);
        operationMap.put("hasInputs", !inputs.isEmpty());
        operationMap.put("inputs", inputs.stream().map(CsInput::toMap).collect(Collectors.toList()));
        operationMap.put("hasOutputs", !outputs.isEmpty());
        operationMap.put("outputs", outputs.stream().map(CsOutput::toMap).collect(Collectors.toList()));
        operationMap.put("javaAction", action.toMap());
        operationMap.put("hasResults", !results.isEmpty());
        operationMap.put("results", results.stream().map(CsResponse::toMap).collect(Collectors.toList()));
        return operationMap;
    }
}
