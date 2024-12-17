package org.example.utils.pipeline;

import lombok.extern.log4j.Log4j2;
import org.example.utils.AppConfig;
import org.example.utils.performance.PerformanceLog;
import org.example.utils.database.DatabaseHandler;

@Log4j2
public class DatabaseStage implements Stage {

    private DatabaseHandler databaseHandler;
    boolean isDatabaseEnabled =  AppConfig
        .getBoolean("feature.database.enabled", false);

    public DatabaseStage() {
        this.databaseHandler = new DatabaseHandler();
    }

    @Override
    public PerformanceLog process(PerformanceLog performanceLog) {
        if (isDatabaseEnabled) {
            databaseHandler.logPerformance(
                performanceLog.getOperationType().getOperation(),
                performanceLog.getOperationDuration()
            );
            log.info("Pipeline saved changes to the database{}", performanceLog);
        }
        return performanceLog;
    }
}
