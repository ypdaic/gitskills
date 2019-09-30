package com.daiyanping.cms.nio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName ServerWithAccpt
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-09-30
 * @Version 0.1
 */
public class ServerWithAccpt {

    private static Selector selector = null;

    private static ServerSocketChannel serverSocketChannel = null;

    public static void main(String[] args) {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 9999);
            ServerSocket socket = serverSocketChannel.socket();
            socket.bind(inetSocketAddress);
            serverSocketChannel.configureBlocking(true);
            SocketChannel accept = serverSocketChannel.accept();
//            selector = accept.socket();
            System.out.println("服务端启动成功");
            new Thread(new Server.ServerTask()).start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    static class ServerTask implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    int select = selector.select();
                    if (select == 0) continue;
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();

                        if (selectionKey.isAcceptable()) {
                            ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                            SocketChannel accept = channel.accept();
                            accept.configureBlocking(false);
                            accept.register(selector, SelectionKey.OP_READ);
                            System.out.println("服务器收到客户端的链接");

                        }

                        if(selectionKey.isReadable()) {
                            SocketChannel channel = (SocketChannel) selectionKey.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            int read = channel.read(byteBuffer);
                            if (read > 0) {

                                byteBuffer.flip();
                                byte[] buffer = new byte[byteBuffer.remaining()];
                                byteBuffer.get(buffer);
                                String s = new String(buffer);
//                                System.out.println("服务器收到的消息：" + s);
//                                String send = "服务器的回应：" + s;
//                                ByteBuffer allocate = ByteBuffer.allocate(send.getBytes().length);
//                                allocate.put(send.getBytes());
//                                allocate.flip();
                                if (!s.equals("isOver")) {

                                    ByteBuffer allocate = ByteBuffer.allocate(buffer.length);
                                    allocate.put(buffer);
                                    allocate.flip();
                                    File file = new File("test2.xls");
                                    FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                                    FileChannel channel1 = fileOutputStream.getChannel();
                                    long position = channel1.position();
                                    System.out.println(position);
                                    while (allocate.hasRemaining()) {

                                        channel1.write(allocate, position);
                                    }

                                    channel1.close();
                                    fileOutputStream.close();
                                }

                                if (s.equals("isOver")) {
                                    channel.close();
                                    selectionKey.cancel();
                                }
                            }
                            if (read < 0) {
                                selectionKey.cancel();
                                channel.close();
                            }
                        }
                        iterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
