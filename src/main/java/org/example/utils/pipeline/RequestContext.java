package org.example.utils.pipeline;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestContext {
    private int treeId;
    private OperationType operationType;
    private double durationMs;

    public RequestContext(int treeId, OperationType operationType) {
        this.treeId = treeId;
        this.operationType = operationType;
    }

}
