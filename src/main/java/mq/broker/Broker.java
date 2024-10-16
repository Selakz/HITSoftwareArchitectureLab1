package mq.broker;

import mq.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public interface Broker {
    Logger logger = LoggerFactory.getLogger(Broker.class);

    void run() throws IOException;

    Message handleSend(String queueName, Message message);

    Message handleReceive(String receiverName);
}
