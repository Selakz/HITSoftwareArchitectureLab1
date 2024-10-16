package mq.client;

import mq.message.Message;

public interface Sender {
    String getName();

    void send(String queueName, Message message);
}
