package part;

import mq.message.BasicMessage;
import mq.message.Message;
import org.junit.Assert;
import org.junit.Test;

public class BasicMessageTest {
    @Test
    public void testConstructorWithSingleString() {
        Message message = new BasicMessage("Publish:Test\r\nContent");
        Assert.assertEquals("Publish:Test", message.getHeader());
        Assert.assertEquals("Content", message.getContent());
    }

    @Test
    public void testConstructorWithContentAndHeaderPairs() {
        Message message = new BasicMessage("Content", "Publish:Test", "Key:Value");
        Assert.assertEquals("Publish:Test Key:Value", message.getHeader());
        Assert.assertEquals("Content", message.getContent());
    }

    @Test
    public void testGetHeader() {
        Message message = new BasicMessage("Content", "Subscribe:Test", "Key:Value");
        Assert.assertEquals("Subscribe:Test Key:Value", message.getHeader());
    }

    @Test
    public void testGetContent() {
        Message message = new BasicMessage("Content", "Key:Value");
        Assert.assertEquals("Content", message.getContent());
    }

    @Test
    public void testToString() {
        Message message = new BasicMessage("Content", "Receive:Test", "Key:Value");
        Assert.assertEquals("Receive:Test Key:Value\r\nContent", message.toString());
    }
}
