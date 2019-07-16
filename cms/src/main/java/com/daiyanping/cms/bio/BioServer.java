package com.daiyanping.cms.bio;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @ClassName BioServer
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-16
 * @Version 0.1
 */
public class BioServer {

//    public static void main(String[] args) {
//        try {
//            ServerSocket serverSocket = new ServerSocket();
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(9999);
//            serverSocket.bind(inetSocketAddress);
//            System.out.println("服务端已启动");
//            while (true) {
//                // 接受客户端的链接，阻塞在这里
//                Socket accept = serverSocket.accept();
//                System.out.println("服务器收到客户端的数据");
//                InputStream inputStream =  accept.getInputStream();
//                OutputStream outputStream =  accept.getOutputStream();
//                outputStream.write("你好:".getBytes());
//                int count = 0;
//                // 数据读取完后，再次读取就会阻塞，除非客户端发送消息过来
//                while ((count = inputStream.read()) > 0) {
//                    outputStream.write(count);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//
//        }
//    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(9999);
            serverSocket.bind(inetSocketAddress);
            System.out.println("服务端已启动");
            while (true) {
                // 接受客户端的链接，阻塞在这里
                Socket accept = serverSocket.accept();
                System.out.println("服务器收到客户端的数据");
                ObjectInputStream inputStream = new ObjectInputStream(accept.getInputStream()) ;
                ObjectOutputStream outputStream = new ObjectOutputStream(accept.getOutputStream()) ;
//                String s = inputStream.readUTF();
//                outputStream.writeUTF("服务端的响应" + s);
//                String s2 = inputStream.readUTF();
//                System.out.println("客户端的回应" + s2);
                outputStream.writeUTF("服务端的响应" + inputStream.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
