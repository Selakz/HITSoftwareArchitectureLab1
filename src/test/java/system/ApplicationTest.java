package system;

import mq.client.Publisher;
import mq.client.Subscriber;
import mq.message.BasicMessage;
import mq.message.Message;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        Assert.assertEquals("q1", ((BasicMessage) msgs1.get(0)).getHeaderValue("Queue"));
        Assert.assertEquals("pbs1", ((BasicMessage) msgs1.get(0)).getHeaderValue("From"));
        Assert.assertEquals("pbs2", ((BasicMessage) msgs1.get(1)).getHeaderValue("From"));
        Assert.assertEquals("pbs1", ((BasicMessage) msgs1.get(2)).getHeaderValue("From"));
        Assert.assertEquals(1, msgs2.size());
        Assert.assertEquals("q2 from pbs1", msgs2.get(0).getContent());
        Assert.assertEquals(0, msgs3.size());
    }

    // 仅供测试用的用户类
    private static class PSUser {
        private final Subscriber subscriber;

        private final ExecutorService threadPool = Executors.newFixedThreadPool(100);

        public PSUser(String name) {
            subscriber = new Subscriber(name);
            subscriber.subscribe(name);
        }

        public void run(int totalCount) {
            new Thread(() -> {
                List<Message> msgs = subscriber.receive();
                do {
                    for (Message msg : msgs) {
                        threadPool.execute(() -> {
                            synchronized (this) {
                                try {
                                    wait(1000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            finishedCount.addAndGet(1);
                        });
                    }
                    msgs = subscriber.receive();
                } while (finishedCount.get() != totalCount);
            }).start();
        }
    }

    // 对，对吗
    public static double getRpsWhen1sPerRequest_PS(int pbsOrSsbCount, int messageCountPerPublisher) {
        finishedCount.set(0);
        List<PSUser> users = new ArrayList<>();
        List<Publisher> publishers = new ArrayList<>();
        final ExecutorService threadPool = Executors.newFixedThreadPool(100);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < pbsOrSsbCount; i++) {
            users.add(new PSUser(Integer.toString(i)));
            Publisher publisher = new Publisher("p" + i);
            String finalI = Integer.toString(i);
            threadPool.execute(() -> {
                for (int j = 0; j < messageCountPerPublisher; j++) {
                    publisher.send(finalI, "any");
                }
            });
        }
        for (int i = 0; i < pbsOrSsbCount; i++) {
            users.get(i).run(pbsOrSsbCount * messageCountPerPublisher);
        }
        while (finishedCount.get() != pbsOrSsbCount * messageCountPerPublisher) ;
        long endTime = System.currentTimeMillis();
        return pbsOrSsbCount * messageCountPerPublisher * 1000.0 / (endTime - startTime);
    }

    public static void main(String[] args) {
        System.out.println(getRpsWhen1sPerRequest_PS(1, 10));
    }
}
