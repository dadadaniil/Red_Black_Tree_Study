package org.example.utils.handler;

public class Request {
    private int treeId;
    private String operationType;
    private double durationMs;

    public Request(int treeId, String operationType) {
        this.treeId = treeId;
        this.operationType = operationType;
    }

    public int getTreeId() {
        return treeId;
    }

    public String getOperationType() {
        return operationType;
    }

    public double getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(double durationMs) {
        this.durationMs = durationMs;
    }
}
