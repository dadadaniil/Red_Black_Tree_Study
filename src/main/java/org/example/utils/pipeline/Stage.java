package org.example.utils.pipeline;

import org.example.utils.performance.PerformanceLog;

@FunctionalInterface
public interface Stage {

    PerformanceLog process(PerformanceLog performanceLog);

}
