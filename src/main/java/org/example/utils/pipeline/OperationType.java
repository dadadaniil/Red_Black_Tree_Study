package org.example.utils.pipeline;

import lombok.Getter;

@Getter
public enum OperationType {
    INSERT("INSERT"),
    DELETE("DELETE");

    private final String operation;

    OperationType(String operation) {
        this.operation = operation;
    }

}
