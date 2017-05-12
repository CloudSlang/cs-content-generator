package io.cloudslang.tools.generator.entities.cs;

/**
 * Author: Ligia Centea
 * Date: 5/16/2016.
 */
public class CsFlowFile {
    private String namespace;
    private CsFlow flow;

    public CsFlowFile(String namespace, CsFlow flow) {
        this.namespace = namespace;
        this.flow = flow;
    }

    public CsFlow getFlow() {
        return flow;
    }

    public void setFlow(CsFlow flow) {
        this.flow = flow;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
