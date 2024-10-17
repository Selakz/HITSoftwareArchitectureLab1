package part;

import mq.message.BasicMessage;
import mq.message.Message;
import mq.queue.PSMQ;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PSMQTest {
    private PSMQ getInstance() {
        PSMQ queue = new PSMQ("testQueue");
        queue.addSubscriber("ssb1");
        queue.addSubscriber("ssb2");
        return queue;
    }

    @Test
    public void testEnqueue() {
        PSMQ queue = getInstance();
        queue.enqueue(new BasicMessage("111"));
        queue.enqueue(new BasicMessage("222"));
        Assert.assertEquals(2, queue.getRemainingCount("ssb1"));
        queue.enqueue(new BasicMessage("333"));
        Assert.assertEquals(3, queue.getRemainingCount("ssb2"));
    }

    @Test
    public void testDequeue() {
        PSMQ queue = getInstance();
        BasicMessage msg1 = new BasicMessage("111"), msg2 = new BasicMessage("222"), msg3 = new BasicMessage("333");
        queue.enqueue(msg1);
        queue.enqueue(msg2);
        queue.enqueue(msg3);
        List<Message> ssb1Ret = queue.dequeue("ssb1", 10);
        Assert.assertEquals(3, ssb1Ret.size());
        Assert.assertEquals(0, queue.getRemainingCount("ssb1"));
        Assert.assertEquals(msg1, ssb1Ret.get(0));
        Assert.assertEquals(msg2, ssb1Ret.get(1));
        Assert.assertEquals(msg3, ssb1Ret.get(2));
        List<Message> ssb2Ret = queue.dequeue("ssb2", 2);
        Assert.assertEquals(2, ssb2Ret.size());
        Assert.assertEquals(msg1, ssb2Ret.get(0));
        Assert.assertEquals(msg2, ssb2Ret.get(1));
        Assert.assertEquals(1, queue.getRemainingCount("ssb2"));
    }

    @Test
    public void testGetRemainingCount() {
        PSMQ queue = getInstance();
        BasicMessage msg1 = new BasicMessage("111"), msg2 = new BasicMessage("222"), msg3 = new BasicMessage("333");
        queue.enqueue(msg1);
        queue.enqueue(msg2);
        queue.enqueue(msg3);
        Assert.assertEquals(0, queue.getRemainingCount("ssb3"));
        queue.addSubscriber("ssb3");
        Assert.assertEquals(0, queue.getRemainingCount("ssb3"));
        queue.enqueue(new BasicMessage("444"));
        Assert.assertEquals(1, queue.getRemainingCount("ssb3"));
    }

    @Test
    public void testContainsSubscriber() {
        PSMQ queue = getInstance();
        Assert.assertTrue(queue.containsSubscriber("ssb1"));
        Assert.assertFalse(queue.containsSubscriber("ssb3"));
    }
}
