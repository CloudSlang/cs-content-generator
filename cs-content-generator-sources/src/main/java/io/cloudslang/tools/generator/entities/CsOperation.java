/*
 * Copyright © 2017 EntIT Software LLC, a Micro Focus company (L.P.)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudslang.tools.generator.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CsOperation implements Mappable {

    private String name;
    private String description;
    private List<CsInput> inputs;
    private List<CsOutput> outputs;
    private CsJavaAction action;
    private List<CsResponse> results;

    public CsOperation(String description, String name, List<CsInput> inputs, List<CsOutput> outputs, CsJavaAction action, List<CsResponse> results) {
        this.description = description;
        this.name = name;
        this.inputs = inputs;
        this.outputs = outputs;
        this.action = action;
        this.results = results;
    }

    public String getDescription() {
        return description;
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
    public CsJavaAction getAction() {
        return action;
    }

    @Override
    public Map<String, Object> toMap() {
        final Map<String, Object> operationMap = new HashMap<>();
        operationMap.put("description", description);
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
