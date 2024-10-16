package mq.queue;

import mq.message.Message;

import java.util.List;

public interface MQ {
    String getName();

    void enqueue(Message message);

    /**
     * 根据receiver返回不超过maxCount数量的Message
     */
    List<Message> dequeue(String receiverName, int maxCount);
}
