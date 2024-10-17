package mq;

import mq.broker.Broker;
import mq.broker.PSBroker;

import java.io.IOException;

public class Application {
    public static void runPS() throws IOException {
        Broker broker = new PSBroker();
        broker.run();
    }

    public static void main(String[] args) throws IOException {
        runPS();
    }
}
