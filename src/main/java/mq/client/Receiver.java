package mq.client;

import mq.message.Message;

import java.util.List;

public interface Receiver {
    String getName();

    List<Message> receive();
}
