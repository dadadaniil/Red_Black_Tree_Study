package org.example.utils.spring;

import org.example.utils.pipeline.DatabaseStage;
import org.example.utils.pipeline.KafkaStage;
import org.example.utils.pipeline.Stage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class StageConfiguration {

    @Value("${bootstrap.servers:localhost:9092}")
    private String bootstrapServers;

    @Bean
    @ConditionalOnProperty(name = "feature.kafka.enabled", havingValue = "true", matchIfMissing = false)
    public Stage kafkaStage() {
        return new KafkaStage(bootstrapServers);
    }

    @Bean
    @ConditionalOnProperty(name = "feature.database.enabled", havingValue = "true", matchIfMissing = false)
    public Stage databaseStage() {
        return new DatabaseStage();
    }
}
