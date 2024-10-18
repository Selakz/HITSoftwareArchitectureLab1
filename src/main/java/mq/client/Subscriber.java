package mq.client;

import mq.config.Config;
import mq.message.BasicMessage;
import mq.message.Message;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Subscriber implements Receiver {
    private final String name;

    private int remainings = 0;

    public Subscriber(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void subscribe(String queueName) {
        Message msg = new BasicMessage("", "Method:Subscribe", "Queue:" + queueName, "From:" + name); // TODO:
        try (Socket socket = new Socket(InetAddress.getLoopbackAddress(), Config.PORT)) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(msg.toString());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BasicMessage result = new BasicMessage(br.readLine());
            if (!result.getHeaderValue("Status").equals("success")) {
                throw new IOException("消息发布失败");
            }
        } catch (IOException e) {
            logger.info(name + "在订阅队列时出现异常");
        }
    }

    @Override
    public List<Message> receive() {
        Message msg = new BasicMessage("", "Method:Receive", "Queue:--ALL--", "From:" + name);
        List<Message> result = new ArrayList<>();
        try (Socket socket = new Socket(InetAddress.getLoopbackAddress(), Config.PORT)) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(msg.toString());
            InputStream ips = socket.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int count;
            while (true) {
                count = ips.read(buffer);
                if (count == -1) break;
                baos.write(buffer, 0, count);
            }
            BasicMessage resMsg = new BasicMessage(baos.toString());
            if (!resMsg.getHeaderValue("Status").equals("success")) {
                throw new IOException("消息接收失败");
            }
            String remainingStr = resMsg.getHeaderValue("Remaining");
            remainings = remainingStr == null ? remainings : Integer.parseInt(remainingStr);
            String[] split = resMsg.getContent().split("\r\n");
            if (split.length % 2 != 0) {
                throw new IOException("消息接收失败");
            }
            for (int i = 0; i < split.length; i += 2) {
                Message newMsg = new BasicMessage(split[i] + "\r\n" + split[i + 1]);
                result.add(newMsg);
            }
        } catch (IOException e) {
            logger.info(name + "在订阅队列时出现异常");
        }
        return result;
    }


    /**
     * 获取还未receive的消息的数量。如果还没进行过任何receive()操作，则返回0
     */
    public int getRemainings() {
        return remainings;
    }
}
