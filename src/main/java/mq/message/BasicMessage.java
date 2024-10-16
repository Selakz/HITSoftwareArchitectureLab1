package mq.message;

public class BasicMessage implements Message {
    // TODO: 应该用Map存储比较好。但是同时希望Publish/Subscribe/Receive在toString时是在最前面的，便于用startsWith判断
    private String header;

    private String content;

    public BasicMessage(String message) {
        // TODO: 根据字符串parse成Message
    }

    /**
     * 分开组装内容，headerPair是每个报头里的键值对："key:pair"
     */
    public BasicMessage(String content, String... headerPair) {

    }

    @Override
    public String getHeader() {
        return header;
    }

    @Override
    public String getContent() {
        return content;
    }

    public String getHeaderValue(String key) {
        return null;
    }

    /**
     * 将报头和报文组合成socket传递的字符串
     */
    @Override
    public String toString() {
        return "";
    }
}
