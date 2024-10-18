package mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Demo {
    public static void main(String[] args) throws IOException {
        String s = "h\r\n";
        System.out.println(s.length() + " " + s.trim().length());
    }
}
