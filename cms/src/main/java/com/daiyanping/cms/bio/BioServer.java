package com.daiyanping.cms.bio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName BioServer
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-16
 * @Version 0.1
 */
public class BioServer {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(9999);
            serverSocket.bind(inetSocketAddress);
            System.out.println("服务端已启动");
            while (true) {
                // 接受客户端的连接，这里是阻塞的
                Socket socket = serverSocket.accept();
                executorService.submit(new ServerTask(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class ServerTask implements Runnable {

        Socket socket;

        public ServerTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            while (true) {

                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {

                    inputStream =  socket.getInputStream();
                    outputStream =  socket.getOutputStream();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int readIndex = 0;
                    int count = 0;
                    while (count == 0) {
                        count = inputStream.available();
                    }


                    int readCount = 0;
                    if (count % 1024 != 0) {
                        readCount = count / 1024 + 1;
                    } else {
                        readCount = count / 1024;
                    }
                    // 数据读取完后，再次读取就会阻塞，除非客户端发送消息过来
                    for (int i = 0; i < readCount; i++) {
                        readIndex = inputStream.read(buffer, readIndex, buffer.length - readIndex);
                        byteArrayOutputStream.write(buffer, 0, readIndex);
                    }
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    System.out.println("服务器收到的数量" + bytes.length);
                    String content = new String(bytes);
                    if (content.startsWith(""))
                    outputStream.write(bytes);
//                    outputStream.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    break;
                } finally {
                }
            }
        }
    }

//    public static void main(String[] args) {
//        try {
//            ServerSocket serverSocket = new ServerSocket();
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(9999);
//            serverSocket.bind(inetSocketAddress);
//            System.out.println("服务端已启动");
//            while (true) {
//                // 接受客户端的链接，阻塞在这里
//                Socket accept = serverSocket.accept();
//                ObjectInputStream inputStream = new ObjectInputStream(accept.getInputStream()) ;
//                ObjectOutputStream outputStream = new ObjectOutputStream(accept.getOutputStream()) ;
////                String s = inputStream.readUTF();
////                outputStream.writeUTF("服务端的响应" + s);
////                String s2 = inputStream.readUTF();
////                System.out.println("客户端的回应" + s2);
//                String s = inputStream.readUTF();
//                System.out.println("服务器收到客户端的数据:" + s);
//                outputStream.writeUTF("服务端的响应" + s);
//                outputStream.flush();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//
//        }
//    }
}
