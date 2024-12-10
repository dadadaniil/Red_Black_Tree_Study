package org.example.utils.pipeline;

import org.example.utils.database.DatabaseHandler;

public class DatabaseStage implements Stage {

    private DatabaseHandler databaseHandler;
    boolean isDatabaseEnabled = Boolean.parseBoolean(System.getProperty("feature.database.enabled", "false"));


    @Override
    public RequestContext process(RequestContext context) {
        if (isDatabaseEnabled) {
            databaseHandler.logPerformance(
                context.getTreeId(),
                context.getOperationType().getOperation(),
                context.getDurationMs()
            );
        }
        return context;
    }
}
