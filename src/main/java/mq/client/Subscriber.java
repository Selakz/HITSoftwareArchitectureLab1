package mq.client;

import mq.message.Message;

import java.util.List;

public class Subscriber implements Receiver {
    private final String name;

    public Subscriber(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void subscribe(String queueName) {
        // 发送subscribe socket，检查返回内容，不成功就抛异常
    }

    @Override
    public List<Message> receive() {
        // 发送receive socket，检查返回内容，将返回内容parse成Message
        return null;
    }
}
