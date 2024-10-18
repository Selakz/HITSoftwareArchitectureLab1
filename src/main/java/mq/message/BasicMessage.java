package mq.message;

import java.util.HashMap;
import java.util.Map;

public class BasicMessage implements Message {
    private final Map<String, String> header = new HashMap<>();
    private final String content;

    public BasicMessage(String message) {
        String[] parts = message.split("\r\n");
        String[] headerLines = parts[0].split(" ");
        for (String headerLine : headerLines) {
            String[] pair = headerLine.split(":");
            if (pair.length == 2) {
                header.put(pair[0].trim(), pair[1].trim());
            }
        }
        StringBuilder rest = new StringBuilder();
        for (int i = 1; i < parts.length; i++) {
            rest.append(parts[i]).append("\r\n");
        }
        content = rest.toString().trim();
    }

    /**
     * 分开组装内容，headerPair是每个报头里的键值对："key:pair"
     */
    public BasicMessage(String content, String... headerPair) {
        this.content = content;
        for (String pair : headerPair) {
            String[] keyValue = pair.split(":");
            if (keyValue.length == 2) {
                header.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
    }

    @Override
    public String getHeader() {
        // 将Map内的报文头部各部分内容按照"key:value"的格式组装输出
        StringBuilder headerString = new StringBuilder();
        for (String key : header.keySet()) {
            headerString.append(key).append(":").append(header.get(key)).append(" ");
        }
        return headerString.toString().trim();
    }

    @Override
    public String getContent() {
        return content;
    }

    public String getHeaderValue(String key) {
        return header.get(key);
    }

    /**
     * 将报头和报文组合成socket传递的字符串
     */
    @Override
    public String toString() {
        return getHeader() + "\r\n" + getContent();
    }
}
