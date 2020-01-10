package com.daiyanping.cms.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReactorHandleMultithreading {

    public static void main(String[] args) throws IOException
    {
        new Thread(new ReactorSingle.Reactor(9999)).start();
    }

    /**
     * 单线程的Reactor，MthreadHandler处理读数据是多线程的
     *
     */
    static class Reactor implements Runnable
    {
        final Selector selector;
        final ServerSocketChannel serverSocket;

        public Reactor(int port) throws IOException
        { //Reactor初始化
            selector = Selector.open();
            serverSocket = ServerSocketChannel.open();
            serverSocket.socket().bind(new InetSocketAddress(port));
            //非阻塞
            serverSocket.configureBlocking(false);

            //分步处理,第一步,接收accept事件，这个sk不用cancel，还要处理后续的连接
            SelectionKey sk =
                    serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            //attach callback object, Acceptor
            sk.attach(new Acceptor());
        }

        public void run()
        {
            try
            {
                while (!Thread.interrupted())
                {
                    selector.select();
                    Set selected = selector.selectedKeys();
                    Iterator it = selected.iterator();
                    while (it.hasNext())
                    {
                        //Reactor负责dispatch收到的事件
                        dispatch((SelectionKey) (it.next()));
                    }
                    selected.clear();
                }
            } catch (IOException ex)
            { /* ... */ }
        }

        void dispatch(SelectionKey k)
        {
            Runnable r = (Runnable) (k.attachment());
            //调用之前注册的callback对象
            if (r != null)
            {
                r.run();
            }
        }

        // inner class 负责接收请求
        class Acceptor implements Runnable
        {
            public void run()
            {
                try
                {
                    // 此处是阻塞的
                    SocketChannel channel = serverSocket.accept();
                    if (channel != null)
                        new MthreadHandler(selector, channel);
                } catch (IOException ex)
                { /* ... */ }
            }
        }
    }

    static class MthreadHandler implements Runnable
    {
        final SocketChannel channel;
        final SelectionKey selectionKey;
        ByteBuffer input = ByteBuffer.allocate(1024);
        ByteBuffer output = ByteBuffer.allocate(1024);
        static final int READING = 0, SENDING = 1;
        int state = READING;


        ExecutorService pool = Executors.newFixedThreadPool(2);
        static final int PROCESSING = 3;

        MthreadHandler(Selector selector, SocketChannel c) throws IOException
        {
            channel = c;
            c.configureBlocking(false);
            // Optionally try first read now
            selectionKey = channel.register(selector, 0);

            //将Handler作为callback对象
            selectionKey.attach(this);

            //第二步,注册Read就绪事件
            selectionKey.interestOps(SelectionKey.OP_READ);
            selector.wakeup();
        }

        boolean inputIsComplete()
        {
            /* ... */
            return false;
        }

        boolean outputIsComplete()
        {

            /* ... */
            return false;
        }

        void process()
        {
            /* ... */
            return;
        }

        public void run()
        {
            try
            {
                if (state == READING)
                {
                    read();
                }
                else if (state == SENDING)
                {
                    send();
                }
            } catch (IOException ex)
            { /* ... */ }
        }


        synchronized void read() throws IOException
        {
            // ...
            channel.read(input);
            if (inputIsComplete())
            {
                state = PROCESSING;
                //使用线程pool异步执行
                pool.execute(new Processer());
            }
        }

        void send() throws IOException
        {
            channel.write(output);

            //write完就结束了, 关闭select key
            if (outputIsComplete())
            {
                selectionKey.cancel();
            }
        }

        synchronized void processAndHandOff()
        {
            process();
            state = SENDING;
            // or rebind attachment
            //process完,开始等待write就绪
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        }

        class Processer implements Runnable
        {
            public void run()
            {
                processAndHandOff();
            }
        }

    }
}