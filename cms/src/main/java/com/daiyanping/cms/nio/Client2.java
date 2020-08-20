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
import java.util.Scanner;
import java.util.Set;

/**
 * @ClassName Client
 * @Description TODO
 * @Author daiyanping
 * @Date 2019-07-19
 * @Version 0.1
 */
public class Client2 {

    private static Selector selector = null;

    private static SocketChannel socketChannel = null;

    private static ClientTask clientTask;

    public static void main(String[] args) {
        try {
            selector = Selector.open();
            socketChannel= SocketChannel.open();
            clientTask = new ClientTask();
            new Thread(clientTask).start();

            Scanner scanner = new Scanner(System.in);
            while (Client2.sendMsg(scanner.next()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    private static boolean sendMsg(String next) {
        clientTask.sendMsg(next);
        return true;
    }

    static class ClientTask implements Runnable {

        public ClientTask() {
            doConnect();

        }

        @Override
        public void run() {
                while (true) {
                    try {
                        int select = selector.select();
                        if (select == 0) continue;
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        SelectionKey selectionKey;
                        while (iterator.hasNext()) {
                            selectionKey  = iterator.next();
                            // 需要将事件给移除掉
                            iterator.remove();
                            try {
                                handleInput(selectionKey);
                            } catch (Exception e) {
                                if (selectionKey != null) {
                                    // 表示取消selectionKey后续的读写事件动作
                                    selectionKey.cancel();
                                }
                            }


                        }
                    } catch (Exception e) {

                    }

                }

//                if (selector != null) {
//                    try {
//                        selector.();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }

        }

        /**
         * 每个SocketChannel只对应一个SelectionKey
         * @param selectionKey
         * @throws IOException
         */
        private void handleInput(SelectionKey selectionKey) throws IOException {
            if (selectionKey.isValid()) {
                /**
                 * 处理连接就绪事件
                 * 但是3次握手未必就成功了，所以需要等待握手完成和判断握手是否成功
                 */
                if (selectionKey.isConnectable()) {

                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    // finishConnect的主要作用就是确认通道连接已建立，方便后续IO操作（读写）不会因为连接没有建立而导致
                    // NotYetConnectedException 异常
                    boolean b = channel.finishConnect();
                    if (b) {
                        System.out.println("客户端已连上服务器");
                        /**
                         * 连接既然已经建立，当然就需要注册读事件，写事件一般是不需要注册的
                         */
                        socketChannel.register(selector, SelectionKey.OP_READ);

                    } else {
                        System.exit(-1);
                    }

                }
                /**
                 * 处理读事件，也就是当前有数据可读
                 */
                if(selectionKey.isReadable()) {
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    // 这里只读取一个字节，如果这次没读取完成，则会触发下一次的读事件，接着继续读取数据
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    // 将channel中的数据读到buffer中
                    int read = channel.read(byteBuffer);
                    // 如果read小于0，表明发生了4次挥手
                    if (read > 0) {
                        byteBuffer.flip();
                        byte[] buffer = new byte[byteBuffer.remaining()];
                        byteBuffer.get(buffer);
                        String s = new String(buffer);
                        System.out.println("客户端收到服务器的消息：" + s);
                    } else {
                        /**
                         * 链路已关闭，释放资源
                         */
                        selectionKey.cancel();
                        channel.close();
                    }

                    throw new RuntimeException("xxxxxxxx");
                }
            }

        }

        /**
         * 对外暴露写API
         * @param next
         */
        public void sendMsg(String next) {
            doWrite(socketChannel, next);
        }

        private void doWrite(SocketChannel socketChannel, String msg) {
            byte[] bytes = msg.getBytes();
            ByteBuffer allocate = ByteBuffer.allocate(bytes.length);
            allocate.put(bytes);
            allocate.flip();
            try {
                socketChannel.write(allocate);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    // 开始连接
    private static void doConnect() {
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 9999);
            // 设置为非阻塞
            socketChannel.configureBlocking(false);
            boolean connect = socketChannel.connect(inetSocketAddress);
            if (connect) {
                // 连接上就注册读事件
                socketChannel.register(selector , SelectionKey.OP_READ);
                System.out.println("客户端已连上服务器");
            } else {
                // 没有连接上就注册连接事件
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
