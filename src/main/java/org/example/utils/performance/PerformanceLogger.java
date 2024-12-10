package org.example.utils.performance;

import org.example.utils.database.DatabaseCommunicator;

import static org.example.utils.performance.TimingUtil.measureExecutionTime;

public class PerformanceLogger {

    private final DatabaseCommunicator postgreSQLDatabaseCommunicator;
    private final int treeId;

    public PerformanceLogger(DatabaseCommunicator postgreSQLDatabaseCommunicator, int treeId) {
        this.postgreSQLDatabaseCommunicator = postgreSQLDatabaseCommunicator;
        this.treeId = treeId;
    }

    public void logAction(String operationType, Runnable action) {
        double duration = measureExecutionTime(operationType, action);
        postgreSQLDatabaseCommunicator.logPerformance(treeId, operationType, duration);
    }
}
