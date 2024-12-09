package org.example.utils.performance;

import org.example.utils.database.DatabaseHandler;

import static org.example.utils.performance.TimingUtil.measureExecutionTime;

public class PerformanceLogger {

    private final DatabaseHandler postgreSQLDatabaseHandler;
    private final int treeId;

    public PerformanceLogger(DatabaseHandler postgreSQLDatabaseHandler, int treeId) {
        this.postgreSQLDatabaseHandler = postgreSQLDatabaseHandler;
        this.treeId = treeId;
    }

    public void logAction(String operationType, Runnable action) {
        double duration = measureExecutionTime(operationType, action);
        postgreSQLDatabaseHandler.logPerformance(treeId, operationType, duration);
    }
}
