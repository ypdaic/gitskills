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
 * @ClassName Server
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-19
 * @Version 0.1
 */
public class Server2 {

    private static Selector selector = null;

    private static ServerSocketChannel serverSocketChannel = null;

    public static void main(String[] args) {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 9999);
            ServerSocket socket = serverSocketChannel.socket();
            socket.bind(inetSocketAddress);
            // 设置为非阻塞模式
            serverSocketChannel.configureBlocking(false);
//
            System.out.println("服务端启动成功");
            new Thread(new ServerTask()).start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {

        }

    }

    static class ServerTask implements Runnable {

        public ServerTask() {
            try {
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // 这里serverSocket交给操作系统管理了
                    int select = selector.select();
                    if (select == 0) continue;
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();

                        /**
                         * 接受连接
                         */
                        if (selectionKey.isAcceptable()) {
                            ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                            SocketChannel sc = channel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.println("服务器收到客户端的链接");

                        }

                        /**
                         * 读消息
                         */
                        if(selectionKey.isReadable()) {
                            System.out.println("===========socket channel 数据准备完成，可以去读取==========");
                            SocketChannel channel = (SocketChannel) selectionKey.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
                            // 读取数据
                            int read = channel.read(byteBuffer);
                            // 读取到数据对数据进行编解码
                            if (read > 0) {
                                // 将缓冲区当前的limit设置为position,position=0
                                // 用于后续对缓冲区的读取操作
                                byteBuffer.flip();
                                // 将缓冲区的数据读取到字节数组
                                byte[] buffer = new byte[byteBuffer.remaining()];
                                byteBuffer.get(buffer);
                                String s = new String(buffer, "UTF-8");
                                System.out.println("服务器收到的消息：" + s);
                                doWrite(s, channel);

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

        private void doWrite(String s, SocketChannel channel) throws IOException {
            String send = "服务器的回应：" + s;
            ByteBuffer allocate = ByteBuffer.allocate(send.getBytes().length);
            allocate.put(send.getBytes());
            allocate.flip();
            channel.write(allocate);
        }
    }
}
