package io.cloudslang.tools.generator.entities.cs;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class CsOperationFile implements Mappable{
    
    private String namespace;
    private CsOperation operation;
    private String description;

    public CsOperationFile(String namespace, CsOperation operation, String description) {
        this.namespace = namespace;
        this.operation = operation;
        this.description = StringUtils.defaultIfEmpty(description, "");
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> csOperationFileMap = new HashMap<>(3);
        csOperationFileMap.put("namespace", namespace);
        csOperationFileMap.put("documentation", description);
        csOperationFileMap.put("operation", operation.toMap());
        return csOperationFileMap;
    }

    public String getNamespace() {
        return namespace;
    }
    public CsOperation getOperation() {
        return operation;
    }

}
