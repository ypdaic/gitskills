package com.daiyanping.cms.bio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @ClassName BioClient
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-16
 * @Version 0.1
 */
public class BioClient {

//    public static void main(String[] args) {
//        try {
//            Socket socket = new Socket();
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(9999);
//            socket.connect(inetSocketAddress);
//            OutputStream outputStream = socket.getOutputStream();
//            outputStream.write(new String("hello bio").getBytes());
//            InputStream inputStream = socket.getInputStream();
//
//            int count = 0;
//            // 数据读取完后，再次读取就会阻塞，除非服务户端发送消息过来，所以直接使用InputStream的话，我们无法再
//            while ((count = inputStream.read()) > 0) {
//                System.out.println(count);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        try {
            Socket socket = new Socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(9999);
            socket.connect(inetSocketAddress);
//            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream()) ;
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream()) ;
            outputStream.write(new String("hello bio").getBytes());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream()) ;

//            String s = inputStream.readUTF();
//            System.out.println("收到服务器的回应" + s);
//
//            outputStream.writeUTF("再次往服务器发送数据");
            System.out.println("收到服务器的回应" + inputStream.readUTF());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
