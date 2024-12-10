package org.example.utils.pipeline;

import org.example.utils.kafka.KafkaProducerUtil;

public class KafkaStage implements Stage {

    private final boolean isKafkaEnabled = Boolean.parseBoolean(
        System.getProperty(
            "feature.database.enabled", "false"
        ));

    private KafkaProducerUtil kafkaProducerUtil;

    @Override
    public RequestContext process(RequestContext context) {
        if (isKafkaEnabled) {
            kafkaProducerUtil.sendEvent( String.valueOf(context.getTreeId()),
                "Operation " + context.getOperationType() + " took " + context.getDurationMs() + " ms"
            ,"");
        }
        return context;
    }
}
