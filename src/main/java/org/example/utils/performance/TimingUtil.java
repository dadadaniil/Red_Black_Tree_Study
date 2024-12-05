package org.example.utils.performance;

import lombok.extern.log4j.Log4j2;

import java.util.function.Consumer;

@Log4j2
public class TimingUtil {

    public static double measureExecutionTime(String methodName, Runnable action) {
        long startTime = System.nanoTime();
        try {
            action.run();
        } finally {
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds
            log.info("{} took {} ms", methodName, durationMs);
            return durationMs;
        }
    }
}
