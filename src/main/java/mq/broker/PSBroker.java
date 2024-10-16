package mq.broker;

import mq.config.Config;
import mq.queue.MQ;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class PSBroker implements Broker {
    private final Set<MQ> queues = new HashSet<>();

    @Override
    public void run() throws IOException {
        ServerSocket server = new ServerSocket(Config.PORT);
        // TODO: Log
        while (true) {
            Socket socket = server.accept();
            // TODO: handle
        }
    }

    @Override
    public void handleSend(String message) {
        // 接收到的socket是send(publish)，则解析报文，存到队列中
    }

    @Override
    public void handleReceive() {
        // 接收到的socket是receive，则解析报文，并返回对应的所有消息
        // 需要再考虑一下具体形式？
    }

    public void handleSubscribe() {
        // 接收到的socket是subscribe，则向对应队列添加该subscriber信息
    }
}
