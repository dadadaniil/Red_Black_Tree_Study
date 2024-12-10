package org.example.utils.handler;

import org.example.utils.database.DatabaseCommunicator;

public class DatabaseHandler extends AbstractHandler {


    private DatabaseCommunicator databaseCommunicator;


    @Override
    public void handle(Request request) {

        databaseCommunicator.logPerformance(
            request.getTreeId(),
            request.getOperationType(),
            request.getDurationMs()
        );
        nextHandle(request);
    }

}

