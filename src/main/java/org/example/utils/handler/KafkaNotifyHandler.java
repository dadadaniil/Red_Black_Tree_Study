package org.example.utils.handler;


import lombok.extern.log4j.Log4j2;

@Log4j2
public class KafkaNotifyHandler extends AbstractHandler {
    @Override
    public void handle(Request request) {
        log.info(request);
    }
}
