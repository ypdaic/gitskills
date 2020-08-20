package com.daiyanping.cms.nio;

import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName Client
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-19
 * @Version 0.1
 */
public class Client {

    private static Selector selector = null;

    private static SocketChannel socketChannel = null;

    public static void main(String[] args) {
        try {
            selector = Selector.open();
            socketChannel= SocketChannel.open();
            InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 9999);
            socketChannel.configureBlocking(false);
            boolean connect = socketChannel.connect(inetSocketAddress);
            if (connect) {
                socketChannel.register(selector, SelectionKey.OP_READ);
                System.out.println("客户端已连上服务器");
            } else {
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }
            new Thread(new ClientTask()).start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    static class ClientTask implements Runnable {

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

                        if (selectionKey.isConnectable()) {

                            SocketChannel channel = (SocketChannel) selectionKey.channel();
                            boolean b = channel.finishConnect();
                            if (b) {
                                System.out.println("客户端已连上服务器");
                                socketChannel.register(selector, SelectionKey.OP_READ);

                                FileSystemResource fileSystemResource = new FileSystemResource("/Users/daiyanping/git-clone-repository/gitskills/cms/src/main/resources/test.txt");
                                InputStream inputStream1 = fileSystemResource.getInputStream();
                                byte[] buffer2 = new byte[1024];
                                int count2 = 0;
                                ByteBuffer allocate = ByteBuffer.allocate(1024);
                                while ((count2 = inputStream1.read(buffer2, count2, buffer2.length - count2)) != -1) {
                                    allocate.put(buffer2, 0, count2);
                                    allocate.flip();
                                    while (allocate.hasRemaining()) {

                                        socketChannel.write(allocate);
                                    }
                                    allocate.clear();
                                }
                                allocate.put("isOver".getBytes());
                                allocate.flip();
                                socketChannel.write(allocate);
                                inputStream1.close();

                            }

                        }

                        if(selectionKey.isReadable()) {
                            SocketChannel channel = (SocketChannel) selectionKey.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
                            int read = channel.read(byteBuffer);
                            if (read > 0) {
                                byteBuffer.flip();
                                byte[] buffer = new byte[byteBuffer.remaining()];
                                byteBuffer.get(buffer);
                                String s = new String(buffer);
                                System.out.println("客户端收到服务器的消息：" + s);
                            }
                        }
                        // 需要将事件给移除掉
                        iterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
