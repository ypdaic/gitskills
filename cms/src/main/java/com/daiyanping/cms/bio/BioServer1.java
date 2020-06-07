package com.daiyanping.cms.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 由于bio阻塞的，所以就有了nio
 */
public class BioServer1 {

    static byte[] bytes = new byte[1024];

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(9090));
        // 这里单线程不支持并发
        while (true) {
            System.out.println("wait conn");
            // 阻塞
            Socket socket = serverSocket.accept();
            System.out.println("conn success");
            System.out.println("wait data");
            // 阻塞 read 读取了多少个字节
            socket.getInputStream().read(bytes);
            System.out.println("data success");
            String s = new String(bytes);
            System.out.println(s);
        }

    }
}
