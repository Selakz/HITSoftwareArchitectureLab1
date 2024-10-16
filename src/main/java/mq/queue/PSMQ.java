package mq.queue;

import mq.message.Message;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Publish / Subscribe模式的MQ
 */
public class PSMQ implements MQ {
    private final String name;
    private final Queue<String> queue = new LinkedList<>(); // ?

    public PSMQ(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void enqueue(Message message) {

    }

    @Override
    public Message dequeue(String receiverName) {
        return null;
    }
}
