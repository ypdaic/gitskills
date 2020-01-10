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

/**
 * @ClassName ReactorSingle
 * @Description TODO
 * @Author daiyanping
 * @Date 2020-01-09
 * @Version 0.1
 */
public class ReactorSingle {

    public static void main(String[] args) throws IOException
    {
        new Thread(new Reactor(9999)).start();
    }

    /**
     * 单线程的Reactor，缺陷就是负责接收连接，处理读写的操作都是在一个线程中
     *
     * 单线程的Reactor缺陷
     * 1、 当其中某个 handler 阻塞时， 会导致其他所有的 client 的 handler 都得不到执行， 并且更严重的是， handler 的阻塞也会导致整个服务不能接收新的 client 请求(因为 acceptor 也被阻塞了)。
     * 因为有这么多的缺陷， 因此单线程Reactor 模型用的比较少。这种单线程模型不能充分利用多核资源，所以实际使用的不多。
     *
     * 2、因此，单线程模型仅仅适用于handler 中业务处理组件能快速完成的场景。
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
                        new Handler(selector, channel);
                } catch (IOException ex)
                { /* ... */ }
            }
        }
    }

    /**
     * 负责处理请求，每个Handler绑定自己的SelectionKey
     */
    static class Handler implements Runnable
    {
        final SocketChannel channel;
        final SelectionKey sk;
        ByteBuffer input = ByteBuffer.allocate(1024);
        ByteBuffer output = ByteBuffer.allocate(1024);
        static final int READING = 0, SENDING = 1;
        int state = READING;

        Handler(Selector selector, SocketChannel c) throws IOException
        {
            channel = c;
            c.configureBlocking(false);
            // Optionally try first read now
            sk = channel.register(selector, 0);

            //将Handler作为callback对象
            sk.attach(this);

            //第二步,注册Read就绪事件
            sk.interestOps(SelectionKey.OP_READ);
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

        void read() throws IOException
        {
            channel.read(input);
            /*
            处理接收到的数据
             */
            if (inputIsComplete())
            {

                process();

                state = SENDING;
                // Normally also do first write now

                //第三步,接收write就绪事件
                sk.interestOps(SelectionKey.OP_WRITE);
            }
        }

        void send() throws IOException
        {
            channel.write(output);

            //write完就结束了, 关闭select key
            if (outputIsComplete())
            {
                sk.cancel();
            }
        }
    }
}
