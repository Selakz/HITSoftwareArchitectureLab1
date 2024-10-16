package mq.broker;

import java.io.IOException;

public interface Broker {
    void run() throws IOException;

    void handleSend(String message);

    void handleReceive();
}
