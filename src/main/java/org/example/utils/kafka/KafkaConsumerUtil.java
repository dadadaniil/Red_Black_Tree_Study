package org.example.utils.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.utils.PerformanceLog;
import org.example.utils.pipeline.OperationType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerUtil {

    private final KafkaConsumer<String, String> consumer;

    public KafkaConsumerUtil(String bootstrapServers, String groupId, String topic) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));
    }

    public PerformanceLog consumePerformanceLog() {
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Received message: Key = %s, Value = %s, Topic = %s, Partition = %d, Offset = %d%n",
                        record.key(), record.value(), record.topic(), record.partition(), record.offset());

                    // Assuming the message value is a serialized PerformanceLog object
                    String[] parts = record.value().split(",");
                    int logId = Integer.parseInt(parts[0]);
                    String operationType = parts[1];
                    float operationDuration = Float.parseFloat(parts[2]);
                    LocalDateTime operationTimestamp = LocalDateTime.parse(parts[3]);

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
        } finally {
            consumer.close();
        }
        return null;
    }

    public void close() {
        consumer.close();
    }
}