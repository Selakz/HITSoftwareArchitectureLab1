package mq.broker;

import mq.config.Config;
import mq.message.BasicMessage;
import mq.message.Message;
import mq.queue.PSMQ;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PSBroker implements Broker {
    private final Set<PSMQ> queues = new HashSet<>();

    private ExecutorService threadPool = Executors.newFixedThreadPool(100);

    @Override
    public void run() throws IOException {
        ServerSocket server = new ServerSocket(Config.PORT);
        logger.info("Broker已在端口" + Config.PORT + "上开始运行");
        while (true) {
            Socket socket = server.accept();
            threadPool.execute(() -> {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String header = br.readLine();
                    String content = br.readLine();
                    if (header.startsWith("Publish")) {
                        logger.info("接收到一条新的Publish请求");
                    }
                    if (header.startsWith("Receive")) {

                    }
                    if (header.startsWith("Subscribe")) {
                        logger.info("接收到一条新的Subscribe请求");
                    }
                } catch (IOException e) {
                    logger.info("处理请求时出现异常");
                }
            });
        }
    }

    @Override
    public Message handleSend(String queueName, Message message) {
        Message msg = new BasicMessage("Status:success\r\n\r\n");
        for (PSMQ q : queues) {
            if (q.getName().equals(queueName)) {
                q.enqueue(message);
                return msg;
            }
        }
        PSMQ newQueue = new PSMQ(queueName);
        newQueue.enqueue(message);
        queues.add(newQueue);
        return msg;
    }

    @Override
    public Message handleReceive(String subscriberName) {
        List<Message> ret = new ArrayList<>();
        int shouldGet = Config.MAX_RECEIVE_COUNT;
        int remainings = 0;
        for (PSMQ q : queues) {
            if (q.containsSubscriber(subscriberName) && shouldGet > 0) {
                List<Message> qRet = q.dequeue(subscriberName, shouldGet);
                ret.addAll(qRet);
                shouldGet -= qRet.size();
            }
            remainings += q.getRemainingCount(subscriberName);
        }
        // TODO: 将这些信息组合成新的Message后返回
        return null;
    }

    public Message handleSubscribe(String subscriberName, String queueName) {
        Message msg = new BasicMessage("Status:success\r\n\r\n");
        for (PSMQ q : queues) {
            if (q.getName().equals(queueName)) {
                q.addSubscriber(subscriberName);
                return msg;
            }
        }
        PSMQ newQueue = new PSMQ(queueName);
        newQueue.addSubscriber(subscriberName);
        queues.add(newQueue);
        return msg;
    }
}
