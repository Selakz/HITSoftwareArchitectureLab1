package mq.client;

import mq.message.Message;

public class Publisher implements Sender {
    private final String name;

    public Publisher(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void send(String queueName, Message message) {

    }
}
