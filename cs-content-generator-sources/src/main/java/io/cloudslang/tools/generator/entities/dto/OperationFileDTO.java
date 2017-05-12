package io.cloudslang.tools.generator.entities.dto;

/**
 * Author: Ligia Centea
 * Date: 4/5/2016.
 */
public class OperationFileDTO {

    private String namespace;
    private OperationDTO operation;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public OperationDTO getOperation() {
        return operation;
    }

    public void setOperation(OperationDTO operationDTO) {
        this.operation = operationDTO;
    }
}
