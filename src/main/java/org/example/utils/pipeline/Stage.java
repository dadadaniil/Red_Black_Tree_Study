package org.example.utils.pipeline;

import org.example.utils.PerformanceLog;

@FunctionalInterface
public interface Stage {
    PerformanceLog process(PerformanceLog performanceLog);

}
