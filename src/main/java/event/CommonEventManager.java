package event;

import java.util.*;

public class CommonEventManager implements EventManager {
    private static CommonEventManager instance = null;
    private final Map<String, List<Listener>> listenerMap;
    
    private CommonEventManager(){
        listenerMap = new HashMap<>();
    }

    public static CommonEventManager getInstance(){
        if(instance == null) instance = new CommonEventManager();
        return instance;
    }

    public void addListener(Listener listener, String eventName) {
        if(listenerMap.containsKey(eventName)){
            listenerMap.get(eventName).add(listener);
        }
        else{
            List<Listener> list = new ArrayList<>();
            list.add(listener);
            listenerMap.put(eventName, list);
        }
    }

    public void trigger(String eventName) {
        if(listenerMap.containsKey(eventName)){
            for(Listener listener : listenerMap.get(eventName)){
                listener.trigger(eventName);
            }
        }
    }
}
