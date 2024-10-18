package system;

import mq.broker.PSBroker;
import mq.client.Publisher;
import mq.client.Subscriber;
import mq.message.BasicMessage;
import mq.message.Message;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 在运行该测试前请先启动Application
 */
public class ApplicationTest {
    public static final AtomicInteger finishedCount = new AtomicInteger(0);

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
        // 测试内容：1. Message内容、数量、顺序是否正确 2. 多个订阅者能否得到同一份消息 3. 是否会收到订阅之前的消息
        Assert.assertEquals(3, msgs1.size());
        Assert.assertEquals("q1", ((BasicMessage) msgs1.get(1)).getHeaderValue("Queue"));
        Assert.assertEquals("pbs2", ((BasicMessage) msgs1.get(2)).getHeaderValue("From"));
        Assert.assertEquals(1, msgs2.size());
        Assert.assertEquals("q2 from pbs1", msgs2.get(0).getContent());
        Assert.assertEquals(0, msgs3.size());
    }

    public double getRpsWhen1sPerRequest_PS(int publisherCount, int messageCountPerPublisher, int subscriberCount) {
        return 0;
    }

    public static void main(String[] args) {

    }
}
