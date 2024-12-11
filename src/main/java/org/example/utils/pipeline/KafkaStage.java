package org.example.utils.pipeline;

import org.example.utils.PerformanceLog;
import org.example.utils.kafka.KafkaConsumerUtil;
import org.example.utils.kafka.KafkaProducerUtil;

public class KafkaStage implements Stage {

    private final boolean isKafkaEnabled = Boolean.parseBoolean(
        System.getProperty(
            "feature.database.enabled", "false"
        ));

    private KafkaProducerUtil kafkaProducerUtil;
    private KafkaConsumerUtil kafkaConsumerUtil;
    private final String topic = "performance-log-topic";

    public KafkaStage(String bootstrapServers) {
        kafkaProducerUtil = new KafkaProducerUtil(bootstrapServers);
        kafkaConsumerUtil = new KafkaConsumerUtil(bootstrapServers, "performance-log-group", topic);
    }

    @Override
    public PerformanceLog process(PerformanceLog performanceLog) {
        if (isKafkaEnabled) {
            kafkaProducerUtil.sendEvent(topic, String.valueOf(performanceLog.getLogId()), performanceLog.toString());

            performanceLog = consumeAndOverridePerformanceLog(performanceLog);
        }
        return performanceLog;
    }

    private PerformanceLog consumeAndOverridePerformanceLog(PerformanceLog performanceLog) {
        performanceLog = kafkaConsumerUtil.consumePerformanceLog();
        return performanceLog;
    }
}
