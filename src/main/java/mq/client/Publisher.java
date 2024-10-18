package mq.client;

import mq.config.Config;
import mq.message.BasicMessage;
import mq.message.Message;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Publisher implements Sender {
    private final String name;

    public Publisher(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void send(String queueName, String content) {
        Message msg = new BasicMessage(content, "Method:Publish", "Queue:" + queueName, "From:" + name);
        try (Socket socket = new Socket("localhost", Config.PORT);
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            bw.write(msg.toString());
            bw.flush();
            socket.shutdownOutput();
            BasicMessage result = new BasicMessage(br.readLine());
            if (!result.getHeaderValue("Status").equals("success")) {
                throw new IOException("消息发布失败");
            }
        } catch (IOException e) {
            logger.info(name + "在发布消息时出现异常");
        }
    }
}
