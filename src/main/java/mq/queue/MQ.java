package mq.queue;

import mq.message.Message;

public interface MQ {
    String getName();

    void enqueue(Message message);

    Message dequeue(String receiverName);
}
