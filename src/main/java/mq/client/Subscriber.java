package mq.client;

import mq.config.Config;
import mq.message.BasicMessage;
import mq.message.Message;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
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
        Message msg = new BasicMessage(""); // TODO:
        try (Socket socket = new Socket(InetAddress.getLoopbackAddress(), Config.PORT)) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(msg.toString());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // TODO: 检查返回的状态码？
        } catch (IOException e) {
            logger.info(name + "在订阅队列时出现异常");
        }
    }

    @Override
    public List<Message> receive() {
        Message msg = new BasicMessage(""); // TODO:
        try (Socket socket = new Socket(InetAddress.getLoopbackAddress(), Config.PORT)) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(msg.toString());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // TODO: 将返回的内容parse成一系列Message，更新remainings
        } catch (IOException e) {
            logger.info(name + "在订阅队列时出现异常");
        }
        return null;
    }


    /**
     * 获取还未receive的消息的数量。如果还没进行过任何receive()操作，则返回0
     */
    public int getRemainings() {
        return remainings;
    }
}
