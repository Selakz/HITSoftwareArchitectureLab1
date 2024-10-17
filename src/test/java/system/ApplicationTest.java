package system;

import mq.broker.PSBroker;
import mq.client.Publisher;
import mq.client.Subscriber;
import mq.message.Message;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * 在运行该测试前请先启动Application
 */
public class ApplicationTest {
    @Test
    public void testFunctions() {
        Subscriber ssb1 = new Subscriber("ssb1"), ssb2 = new Subscriber("ssb2"), ssb3 = new Subscriber("ssb3");
        ssb1.subscribe("q1");
        ssb1.subscribe("q2");
        ssb2.subscribe("q2");
        Publisher pbs1 = new Publisher("pbs1"), pbs2 = new Publisher("pbs2"), pbs3 = new Publisher("pbs3");
        pbs1.send("q1", "q1 from pbs1");
        pbs1.send("q2", "q2 from pbs1");
        pbs2.send("q1", "q1 from pbs2");
        pbs3.send("q3", "q3 from pbs3");
        ssb3.subscribe("q3");
        List<Message> msgs1 = ssb1.receive();
        List<Message> msgs2 = ssb2.receive();
        List<Message> msgs3 = ssb3.receive();
        // TODO: 各种assert操作
    }

    public static void main(String[] args) {
        boolean isOver = false;
        int requestCount = 0;
        Subscriber ssb = new Subscriber("ssb");
        Publisher pbs = new Publisher("pbs");
        final long startTime = System.currentTimeMillis();
        // TODO: pbs发发发，ssb收收收

        final long endTime = System.currentTimeMillis();
        System.out.println("Rps: " + requestCount * 1000 / (endTime - startTime));
    }
}
