package org.example;


import lombok.extern.log4j.Log4j2;
import org.example.storage.tree.RedBlackTree;
import org.example.utils.PerformanceLog;
import org.example.utils.TreeUtils;
import org.example.utils.pipeline.OperationType;
import org.example.utils.pipeline.PerformanceLogPipeline;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static org.example.utils.TreeUtils.createTreeFromFile;


@Log4j2
@Component
public class Main implements CommandLineRunner {

    private final PerformanceLogPipeline performanceLogPipeline;

    public Main(PerformanceLogPipeline performanceLogPipeline) {
        this.performanceLogPipeline = performanceLogPipeline;
    }

    @Override
    public void run(String... args) {
        treeCreation();
    }

    private void treeCreation() {
        log.info("Starting application");

        PerformanceLog performanceLog = PerformanceLog.builder()
            .logId(1)
            .operationType(OperationType.INSERT)
            .operationDuration(123.45f)
            .operationTimestamp(LocalDateTime.now())
            .build();
        performanceLogPipeline.execute(performanceLog);

    }

    private static void createTree(String[] args) {
        String path = "data/tree_data.txt";
        if (args.length > 0) {
            path = args[0];
        }
        log.info("Creating tree from file by path: {}", path);

        RedBlackTree<Integer> intTree = createTreeFromFile(path);
        TreeUtils.printTree(intTree);
    }
}