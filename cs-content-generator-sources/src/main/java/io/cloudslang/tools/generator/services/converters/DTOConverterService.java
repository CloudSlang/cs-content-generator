package io.cloudslang.tools.generator.services.converters;

import io.cloudslang.tools.generator.entities.cs.CsFlow;
import io.cloudslang.tools.generator.entities.cs.CsFlowFile;
import io.cloudslang.tools.generator.entities.cs.CsInput;
import io.cloudslang.tools.generator.entities.cs.CsJavaAction;
import io.cloudslang.tools.generator.entities.cs.CsOperation;
import io.cloudslang.tools.generator.entities.cs.CsOperationFile;
import io.cloudslang.tools.generator.entities.cs.CsOutput;
import io.cloudslang.tools.generator.entities.cs.CsResponse;
import io.cloudslang.tools.generator.entities.cs.CsTask;
import io.cloudslang.tools.generator.entities.dto.FlowDTO;
import io.cloudslang.tools.generator.entities.dto.FlowFileDTO;
import io.cloudslang.tools.generator.entities.dto.OperationDTO;
import io.cloudslang.tools.generator.entities.dto.OperationFileDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Author: Ligia Centea
 * Date: 4/5/2016.
 */
@Service
public class DTOConverterService {

    @Autowired
    private MapConverterService mapConverterService;

    public OperationFileDTO getOperationFileDTO(CsOperationFile operationFile) {
        Assert.notNull(operationFile);

        OperationDTO operationDTO = new OperationDTO();
        CsOperation operation = operationFile.getOperation();

        operationDTO.setName(operation.getName());
        operationDTO.setInputs(getInputsMap(operation.getInputs()));
        operationDTO.setJava_action(getActionMap(operation.getAction()));
        operationDTO.setOutputs(getOutputs(operation.getOutputs()));
        operationDTO.setResults(getResults(operation.getResults()));

        OperationFileDTO operationFileDTO = new OperationFileDTO();
        operationFileDTO.setNamespace(operationFile.getNamespace());
        operationFileDTO.setOperation(operationDTO);
        return operationFileDTO;
    }

    public FlowFileDTO getFlowFileDTO(CsFlowFile flowFile) {
        Assert.notNull(flowFile);

        FlowDTO flowDTO = new FlowDTO();
        CsFlow operation = flowFile.getFlow();

        flowDTO.setName(operation.getName());
        flowDTO.setInputs(getInputsMap(operation.getInputs()));
        flowDTO.setOutputs(getOutputs(operation.getOutputs()));
        flowDTO.setResults(getResults(operation.getResponses()));
        flowDTO.setWorkflow(getWorkflow(operation.getTasks()));

        FlowFileDTO flowFileDTO = new FlowFileDTO();
        flowFileDTO.setNamespace(flowFile.getNamespace());
        flowFileDTO.setFlow(flowDTO);

        return flowFileDTO;
    }

    private List<Map<String, String>> getWorkflow(List<CsTask> tasks) {
        return null;
    }

    private List<Map<String, Map<String, Object>>> getInputsMap(List<CsInput> inputs) {
        return inputs.stream().map(this::getInputAsMap).collect(Collectors.toList());
    }

    private Map<String, Object> getActionMap(CsJavaAction action) {
        return mapConverterService.getObjectAsMap(action);
    }

    private Map<String, Map<String, Object>> getInputAsMap(CsInput input) {
        return buildMap(input.getName(), mapConverterService.getObjectAsMap(input));
    }

    private List<Object> getOutputs(List<CsOutput> outputs) {
        return outputs.stream()
                .map(o -> o.getExpression() == null ? o.getName() : buildMap(o.getName(), o.getExpression()))
                .collect(Collectors.toList());
    }

    private List<Object> getResults(List<CsResponse> results) {
        return results.stream()
                .map(o -> o.getRule() == null ? o.getName() : buildMap(o.getName(), o.getRule()))
                .sorted((o1, o2) -> {
                    //put Maps before strings
                    if (o1 instanceof Map) {
                        return -1;
                    } else {
                        return 1;
                    }
                })
                .collect(Collectors.toList());
    }

    private <T> Map<String, T> buildMap(String key, T value) {
        Map<String, T> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

}
