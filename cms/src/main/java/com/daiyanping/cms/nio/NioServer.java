package com.daiyanping.cms.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.DoubleToIntFunction;

public class NioServer {

    static List<SocketChannel> list = new ArrayList<SocketChannel>();

    static ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 9090));
//        设置ServerSocket为非阻塞的，让代码不用在accept阻塞，如果accept阻塞了，当在等待客户端连接时，之前已经连接过的客户端
//        发送数据过来就没法接受了
        serverSocketChannel.configureBlocking(false);
        while (true) {
//            由于这里不阻塞，当没有客户端连接时，这里的socketChannel就是null
            SocketChannel socketChannel = serverSocketChannel.accept();

            if (Objects.isNull(socketChannel)) {
//                没有连接睡眠500ms
                try {
                    Thread.sleep(500);
                    System.out.println("no conn");
//                    但这里轮训会存在性能问题，但1000个客户端连接后，就是不发消息，我这里也是在空轮训
//                    所以这个socketChannel交给应用程序是不可以的，所以就出现了socketChannel.register(selector, SelectionKey.OP_READ);
                    for (SocketChannel socketChannel1: list) {
                        byteBuffer.clear();
                        int read = socketChannel1.read(byteBuffer);
                        byteBuffer.flip();
                        if (read > 0) {
                            System.out.println(new String(byteBuffer.array()));

                        }

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("conn-----------------------------");
                //            设置其为非阻塞的，让代码不用在这里等待客户端输入
                socketChannel.configureBlocking(false);
                list.add(socketChannel);
                for (SocketChannel socketChannel1: list) {
                    byteBuffer.clear();
                    int read = socketChannel1.read(byteBuffer);
                    byteBuffer.flip();
                    if (read > 0) {
                        System.out.println(new String(byteBuffer.array()));

                    }

                }

            }

        }

    }
}
