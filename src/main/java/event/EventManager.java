package event;

public interface EventManager {
    void addListener(Listener listener, String eventName);
    void trigger(String eventName);
}
