package org.example.utils.pipeline;

import org.example.utils.PerformanceLog;
import org.example.utils.database.DatabaseHandler;

public class DatabaseStage implements Stage {

    private DatabaseHandler databaseHandler;
    boolean isDatabaseEnabled = Boolean.parseBoolean(System.getProperty("feature.database.enabled", "false"));


    @Override
    public PerformanceLog process(PerformanceLog performanceLog) {
        if (isDatabaseEnabled) {
            databaseHandler.logPerformance(
                performanceLog.getOperationType().getOperation(),
                performanceLog.getOperationDuration()
            );
        }
        return performanceLog;
    }
}
