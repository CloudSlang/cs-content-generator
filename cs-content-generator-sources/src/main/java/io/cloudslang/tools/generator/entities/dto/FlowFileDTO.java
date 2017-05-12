package io.cloudslang.tools.generator.entities.dto;

/**
 * Author: Ligia Centea
 * Date: 5/17/2016.
 */
public class FlowFileDTO {
    private String namespace;
    private FlowDTO flow;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public FlowDTO getFlow() {
        return flow;
    }

    public void setFlow(FlowDTO flow) {
        this.flow = flow;
    }
}
