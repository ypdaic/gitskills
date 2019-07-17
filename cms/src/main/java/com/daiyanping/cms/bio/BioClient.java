package com.daiyanping.cms.bio;

import org.springframework.core.io.FileSystemResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName BioClient
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-16
 * @Version 0.1
 */
public class BioClient {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Socket socket = new Socket();
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(9999);
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            FileSystemResource fileSystemResource = new FileSystemResource("/Users/daiyanping/git-clone-repository/gitskills/cms/src/main/resources/test.xls");
            InputStream inputStream1 = fileSystemResource.getInputStream();
            byte[] buffer2 = new byte[1024];
            int count2 = 0;
            while ((count2 = inputStream1.read(buffer2, count2, buffer2.length - count2)) != -1) {
                byteArrayOutputStream.write(buffer2, 0, count2);
            }

            byte[] bytes1 = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.flush();
            String heard = "CoentLength:" + bytes1.length + " ";
            outputStream.write(heard.getBytes());
            System.out.println("客户端发送的数量" + bytes1.length);
            executorService.submit(new ClientTask(socket));

            outputStream.write(bytes1);
//            outputStream.flush();
//                InputStream inputStream = socket.getInputStream();
//                byte[] buffer = new byte[1024];
//                int readIndex = 0;
//                int count = 0;
//                while (count == 0) {
//                    count = inputStream.available();
//                }
//
//
//                int readCount = 0;
//                if (count % 1024 != 0) {
//                    readCount = count / 1024 + 1;
//                } else {
//                    readCount = count / 1024;
//                }
//
//
//                // 数据读取完后，再次读取就会阻塞，除非客户端发送消息过来
//                for (int i = 0; i < readCount; i++) {
//                    readIndex = inputStream.read(buffer, readIndex, buffer.length - readIndex);
//                    byteArrayOutputStream.write(buffer, 0, readIndex);
//                }
//                bytes = byteArrayOutputStream.toByteArray();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static class ClientTask implements Runnable {

        Socket socket;

        public ClientTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            while (true) {

                InputStream inputStream = null;
                ByteArrayOutputStream byteArrayOutputStream = null;
                try {
                    inputStream = socket.getInputStream();
                    byteArrayOutputStream = new ByteArrayOutputStream();
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
                    System.out.println("服务端发送回来的数据" + new String(bytes));
                } catch (Exception e) {
                    e.printStackTrace();
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    break;
                }
            }
        }
    }

//    public static void main(String[] args) {
//        try {
//            Socket socket = new Socket();
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(9999);
//            socket.connect(inetSocketAddress);
////            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream()) ;
//            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream()) ;
//            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream()) ;
//            outputStream.writeUTF("hello bio");
////            使用write,read 函数读写时，数据都是先写到缓存区，而不是直接写到网络上，等到一定的数据积压
////            才会将缓冲区的数据写往网络，而这是TCP协议控制的，必须我们收到调用flush函数将数据刷到网络上
//            outputStream.flush();
//            System.out.println("收到服务器的回应" + inputStream.readUTF());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public static void main(String[] args) {
//        try {
//            Socket socket = new Socket();
//            InetSocketAddress inetSocketAddress = new InetSocketAddress(9999);
//            socket.connect(inetSocketAddress);
//            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
//            BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
//            bufferedOutputStream.write("h".getBytes());
////            使用write,read 函数读写时，数据都是先写到缓存区，而不是直接写到网络上，等到一定的数据积压
////            才会将缓冲区的数据写往网络，而这是TCP协议控制的，必须我们收到调用flush函数将数据刷到网络上
//            bufferedOutputStream.flush();
//            System.out.println("收到服务器的回应" + bufferedInputStream.read());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
