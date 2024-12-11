package org.example.utils.pipeline;

import lombok.extern.log4j.Log4j2;
import org.example.utils.AppConfig;
import org.example.utils.PerformanceLog;
import org.example.utils.kafka.KafkaConsumerUtil;
import org.example.utils.kafka.KafkaProducerUtil;

import java.util.function.Function;

@Log4j2
public class KafkaStage implements Stage {

    private final boolean isKafkaEnabled = AppConfig
        .getBoolean("feature.kafka.enabled", false);

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
            log.info("Pipeline notified Kafka with {}", performanceLog);
        }
        return performanceLog;
    }

    private PerformanceLog consumeAndOverridePerformanceLog(PerformanceLog performanceLog) {
        performanceLog = kafkaConsumerUtil.consumePerformanceLog();
        return performanceLog;
    }

}
