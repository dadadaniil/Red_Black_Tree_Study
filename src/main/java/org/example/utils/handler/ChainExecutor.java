package org.example.utils.handler;

public class ChainExecutor {

    private Handler chainHead;

    public ChainExecutor() {
        TimingHandler timingHandler = new TimingHandler();
        KafkaNotifyHandler kafkaHandler = new KafkaNotifyHandler();
        DatabaseHandler dbHandler = new DatabaseHandler();

        timingHandler.setNext(kafkaHandler);
        kafkaHandler.setNext(dbHandler);

        this.chainHead = timingHandler;
    }

    public void executeChain(int treeId, String operationType) {
        Request request = new Request(treeId, operationType);
        chainHead.handle(request);
    }
}
