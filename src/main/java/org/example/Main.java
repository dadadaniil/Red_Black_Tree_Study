package org.example;


import lombok.extern.log4j.Log4j2;
import org.example.storage.tree.RedBlackTree;
import org.example.utils.PerformanceLog;
import org.example.utils.TreeUtils;
import org.example.utils.kafka.KafkaConsumerUtil;
import org.example.utils.kafka.KafkaProducerUtil;
import org.example.utils.pipeline.DatabaseStage;
import org.example.utils.pipeline.KafkaStage;
import org.example.utils.pipeline.OperationType;
import org.example.utils.pipeline.Pipeline;

import java.time.LocalDateTime;

import static org.example.utils.TreeUtils.createTreeFromFile;

@Log4j2
public class Main {
    public static void main(String[] args) {
        log.info("Starting application");

        String bootstrapServers = "localhost:9092";
        String topic = "performance-log-topic";


        Pipeline pipeline = new Pipeline()
            .addStage(new DatabaseStage());



        PerformanceLog performanceLog = PerformanceLog.builder()
            .logId(1)
            .operationType(OperationType.INSERT)
            .operationDuration(123.45f)
            .operationTimestamp(LocalDateTime.now())
            .build();

        PerformanceLog result = pipeline.execute(performanceLog);


    }

    private static void produceConsumeKafkaTest() {

        String bootstrapServers = "localhost:9092";
        String topic = "performance-log-topic";

        KafkaProducerUtil producer = new KafkaProducerUtil(bootstrapServers);

        PerformanceLog performanceLog = PerformanceLog.builder()
            .operationType(OperationType.INSERT)
            .operationDuration(123.45f)
            .operationTimestamp(LocalDateTime.now())
            .build();

        producer.sendEvent(topic, String.valueOf(performanceLog.getLogId()), performanceLog.toString());
        log.info("Sent PerformanceLog: {}", performanceLog);

        KafkaConsumerUtil consumer = new KafkaConsumerUtil(bootstrapServers, "performance-log-group", topic);

        PerformanceLog receivedLog = consumer.consumePerformanceLog();
        if (receivedLog != null) {
            log.info("Received PerformanceLog: {}", receivedLog);
        } else {
            log.warn("No PerformanceLog received.");
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            producer.close();
            consumer.close();
            log.info("Producer and consumer closed.");
        }));

        log.info("Application started");
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