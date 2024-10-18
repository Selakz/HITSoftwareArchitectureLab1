package mq.broker;

import mq.config.Config;
import mq.message.BasicMessage;
import mq.message.Message;
import mq.queue.PSMQ;

import java.io.*;
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

    private final ExecutorService threadPool = Executors.newFixedThreadPool(100);

    @Override
    public void run() throws IOException {
        ServerSocket server = new ServerSocket(Config.PORT);
        logger.info("Broker已在端口" + Config.PORT + "上开始运行");
        while (true) {
            Socket socket = server.accept();
            threadPool.execute(() -> {
                try {
                    InputStream ips = socket.getInputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int count;
                    while (true) {
                        count = ips.read(buffer);
                        if (count == -1) break;
                        baos.write(buffer, 0, count);
                    }
                    BasicMessage resMsg = new BasicMessage(baos.toString());
                    Message returnMsg;
                    switch (resMsg.getHeaderValue("Method")) {
                        case "Publish":
                            String queueName = resMsg.getHeaderValue("Queue");
                            returnMsg = handleSend(queueName, resMsg);
                            break;
                        case "Receive":
                            if (resMsg.getHeaderValue("From") == null) {
                                returnMsg = new BasicMessage("", "Status:invalid");
                                break;
                            }
                            String receiverName = resMsg.getHeaderValue("From");
                            returnMsg = handleReceive(receiverName);
                            break;
                        case "Subscribe":
                            if (resMsg.getHeaderValue("From") == null) {
                                returnMsg = new BasicMessage("", "Status:invalid");
                                break;
                            }
                            String subscriberName = resMsg.getHeaderValue("From");
                            String toQueueName = resMsg.getHeaderValue("Queue");
                            returnMsg = handleSubscribe(subscriberName, toQueueName);
                            break;
                        default:
                            returnMsg = new BasicMessage("", "Status:invalid");
                            break;
                    }
                    bw.write(returnMsg.toString());
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    logger.info("处理请求时出现异常");
                }
            });
        }
    }

    @Override
    public Message handleSend(String queueName, Message message) {
        Message msg = new BasicMessage("", "Status:success");
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
        StringBuilder sb = new StringBuilder();
        for (Message msg : ret) {
            sb.append(msg.getHeader()).append("\r\n").append(msg.getContent()).append("\r\n");
        }
        return new BasicMessage(sb.toString(), "Status:success", "Remaining:" + remainings);
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
