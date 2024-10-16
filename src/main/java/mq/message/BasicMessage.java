package mq.message;

public class BasicMessage implements Message {
    private String header;
    private String content;

    public BasicMessage(String message) {
        // TODO: 根据字符串parse成Message
    }

    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public String getContent() {
        return content;
    }

    /**
     * 将报头和报文组合成socket传递的字符串
     */
    @Override
    public String toString() {
        return "";
    }
}
