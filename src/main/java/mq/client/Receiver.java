package mq.client;

import mq.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface Receiver {
    Logger logger = LoggerFactory.getLogger(Receiver.class);

    String getName();

    List<Message> receive();
}
