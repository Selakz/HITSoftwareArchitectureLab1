package mq.client;

import mq.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Sender {
    Logger logger = LoggerFactory.getLogger(Sender.class);

    String getName();

    void send(String queueName, String content);
}
