package event;

public class ExampleListener implements Listener {
    @Override
    public void trigger(String eventName) {
        if(eventName.equals("Test")){
            System.out.println("test event trigger");
        }
    }
}
