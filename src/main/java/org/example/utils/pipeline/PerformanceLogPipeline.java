package org.example.utils.pipeline;

import org.example.utils.PerformanceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PerformanceLogPipeline {
    private List<Stage> stages;


    @Autowired
    public PerformanceLogPipeline(List<Stage> stages){
        this.stages=stages;
    }

    public PerformanceLogPipeline addStage(Stage stage) {
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
