package org.example.utils.pipeline;

import org.example.utils.PerformanceLog;

import java.util.ArrayList;
import java.util.List;

public class Pipeline {
    private List<Stage> stages = new ArrayList<>();

    public Pipeline addStage(Stage stage) {
        stages.add(stage);
        return this;
    }

    public PerformanceLog execute(PerformanceLog performanceLog) {
        for (Stage stage : stages) {
            performanceLog = stage.process(performanceLog);
        }
        return performanceLog;
    }
}
