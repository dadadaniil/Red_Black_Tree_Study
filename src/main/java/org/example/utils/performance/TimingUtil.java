package org.example.utils.performance;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class TimingUtil {
    public static void measureExecutionTime(String methodName, Runnable action) {
        long startTime = System.nanoTime();
        action.run();
        long endTime = System.nanoTime();
        log.info("{} took {} ns", methodName, endTime - startTime);
    }
}
