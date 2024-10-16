package mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class Demo {
    public static void main(String[] args) throws IOException {
        Logger logger = LoggerFactory.getLogger(Demo.class);
        logger.info("Demo logged");
    }
}
