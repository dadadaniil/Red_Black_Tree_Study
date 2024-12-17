package org.example.utils.kafka;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.example.utils.performance.PerformanceLog;
import org.example.utils.pipeline.OperationType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Properties;

@Log4j2
@Component
public class KafkaConsumerUtil {

    private final KafkaConsumer<String, String> consumer;

    public KafkaConsumerUtil(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
                             @Value("${spring.kafka.consumer.group-id}") String groupId,
                             @Value("${spring.kafka.consumer.topic}") String topic) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            "org.apache.kafka.common.serialization.StringDeserializer"
        );
        props.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            "org.apache.kafka.common.serialization.StringDeserializer"
        );
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));
    }

    public PerformanceLog consumePerformanceLog() {
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                if (records.isEmpty()) {
                    log.info("No records found, waiting...");
                    continue;
                }

                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Received message: Key = %s, Value = %s, Topic = %s, Partition = %d, Offset = %d%n",
                        record.key(), record.value(), record.topic(), record.partition(), record.offset()
                    );

                    String value = record.value();
                    String[] parts = value.split(", ");
                    String operationType = parts[1].split("=")[1];
                    float operationDuration = Float.parseFloat(parts[2].split("=")[1]);
                    LocalDateTime operationTimestamp = LocalDateTime.parse(parts[3].split("=")[1].replace(")", ""));

                    int logId = (int) record.offset();

                    return PerformanceLog
                        .builder()
                        .logId(logId)
                        .operationType(OperationType.valueOf(operationType))
                        .operationDuration(operationDuration)
                        .operationTimestamp(operationTimestamp)
                        .build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void close() {
        consumer.close();
    }
}