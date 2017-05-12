package io.cloudslang.tools.generator.entities.cs;

/**
 * Author: Ligia Centea
 * Date: 4/5/2016.
 */
public class CsOperationFile {
    
    private String namespace;
    private CsOperation operation;

    public CsOperationFile() {
    }

    public CsOperationFile(String namespace, CsOperation operation) {
        this.namespace = namespace;
        this.operation = operation;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public CsOperation getOperation() {
        return operation;
    }

    public void setOperation(CsOperation operation) {
        this.operation = operation;
    }
}
