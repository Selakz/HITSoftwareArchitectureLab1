package event;

/**
 * 不走网络通信的丐版消息中间件
 */
public class Demo {
    public static void main(String[] arg) {
        ExampleListener listener = new ExampleListener();
        CommonEventManager.getInstance().addListener(listener, "Test");
        CommonEventManager.getInstance().trigger("Test");
    }
}
