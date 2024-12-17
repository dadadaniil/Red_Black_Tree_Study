package org.example.utils.spring;

import lombok.extern.log4j.Log4j2;
import org.example.utils.pipeline.DatabaseStage;
import org.example.utils.pipeline.KafkaStage;
import org.example.utils.pipeline.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
public class StageConfiguration {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Bean
    @ConditionalOnProperty(prefix = "feature", name = "kafka", havingValue = "true")
    public Stage kafkaStage() {
        log.info("Creating KafkaStage with bootstrapServers {}", bootstrapServers);
        return new KafkaStage(bootstrapServers);
    }

    @Bean
    @ConditionalOnProperty(prefix = "feature", name = "database", havingValue = "true")
    public Stage databaseStage() {
        return new DatabaseStage();
    }
}
