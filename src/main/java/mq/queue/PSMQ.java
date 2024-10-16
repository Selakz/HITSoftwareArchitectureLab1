package mq.queue;

import mq.config.Config;
import mq.message.Message;

import java.util.*;

/**
 * Publish / Subscribe模式的MQ
 */
public class PSMQ implements MQ {
    private final String name;
    private final LinkedList<Message> queue = new LinkedList<>(); // ?
    private final Map<String, Integer> subscribePointers = new HashMap<>();

    public PSMQ(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void enqueue(Message message) {
        queue.add(message);
    }

    @Override
    public List<Message> dequeue(String subscriberName, int maxCount) {
        List<Message> ret = new ArrayList<>();
        if (!subscribePointers.containsKey(subscriberName)) return ret;
        int pointer = subscribePointers.get(subscriberName), dequeueCount = 0;
        while (dequeueCount < maxCount && pointer + dequeueCount < queue.size()) {
            ret.add(queue.get(pointer));
            dequeueCount++;
        }
        subscribePointers.put(subscriberName, pointer + dequeueCount);
        removeUsedUps();
        return ret;
    }

    public int getRemainingCount(String subscriberName) {
        if (!subscribePointers.containsKey(subscriberName)) return 0;
        return queue.size() - subscribePointers.get(subscriberName);
    }

    public void addSubscriber(String subscriberName) {
        subscribePointers.put(subscriberName, queue.size());
    }

    public boolean containsSubscriber(String subscriberName) {
        return subscribePointers.containsKey(subscriberName);
    }

    /**
     * 将已经被全部订阅者读取完的消息移除
     */
    private void removeUsedUps() {
        if (subscribePointers.isEmpty()) return;
        int minPointer = Integer.MAX_VALUE;
        for (String name : subscribePointers.keySet()) {
            minPointer = Math.min(minPointer, subscribePointers.get(name));
        }
        for (String name : subscribePointers.keySet()) {
            subscribePointers.put(name, subscribePointers.get(name) - minPointer);
        }
        for (int i = 0; i < minPointer; i++) queue.remove();
    }
}
