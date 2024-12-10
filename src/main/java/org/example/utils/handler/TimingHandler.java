package org.example.utils.handler;

import org.example.utils.performance.TimingUtil;

public class TimingHandler extends AbstractHandler {
    @Override
    public void handle(Request request) {
        double durationMs = TimingUtil.measureExecutionTime(
            request.getOperationType(),
            () -> {

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        );

        request.setDurationMs(durationMs);
        nextHandle(request);
    }
}

