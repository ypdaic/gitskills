package com.daiyanping.cms.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReactorAndHandleMultithreading {

    public static void main(String[] args) throws IOException
    {
        new Thread(new MthreadReactor(9999)).start();
    }

    /**
     * 多线程的Reactor，MthreadHandler处理读数据是多线程的
     *
     */
    static class MthreadReactor implements Runnable
    {
        //subReactors集合, 一个selector代表一个subReactor
        Selector selector;
        int next = 0;
        final ServerSocketChannel serverSocket;

        MthreadReactor(int port) throws IOException
        { //Reactor初始化
            selector = Selector.open();
            serverSocket = ServerSocketChannel.open();
            serverSocket.socket().bind(new InetSocketAddress(port));
            //非阻塞
            serverSocket.configureBlocking(false);


            //分步处理,第一步,接收accept事件
            SelectionKey sk =
                    serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            //attach callback object, Acceptor
            sk.attach(new MultiWorkThreadAcceptor());
        }

        public void run()
        {
            try
            {
                while (!Thread.interrupted())
                {
                    for (int i = 0; i <2 ; i++)
                    {
                        selector.select();
                        Set selected =  selector.selectedKeys();
                        Iterator it = selected.iterator();
                        while (it.hasNext())
                        {
                            //Reactor负责dispatch收到的事件
                            dispatch((SelectionKey) (it.next()));
                        }
                        selected.clear();
                    }

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


        /**
         * 多work 连接事件Acceptor,处理连接事件
         */
        class MultiWorkThreadAcceptor implements Runnable {
​
            // cpu线程数相同多work线程
            int workCount =Runtime.getRuntime().availableProcessors();
            SubReactor[] workThreadHandlers = new SubReactor[workCount];
            volatile int nextHandler = 0;
​
            public MultiWorkThreadAcceptor() {
                this.init();
            }
​
            public void init() {
                nextHandler = 0;
                for (int i = 0; i < workThreadHandlers.length; i++) {
                    try {
                        workThreadHandlers[i] = new SubReactor();
                    } catch (Exception e) {
                    }
​
                }
            }
​
            @Override
            public void run() {
                try {
                    SocketChannel c = serverSocket.accept();
                    if (c != null) {// 注册读写
                        synchronized (c) {
                            // 顺序获取SubReactor，然后注册channel
                            SubReactor work = workThreadHandlers[nextHandler];
                            work.registerChannel(c);
                            nextHandler++;
                            if (nextHandler >= workThreadHandlers.length) {
                                nextHandler = 0;
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }

    }

    /**
     * 多work线程处理读写业务逻辑
     */
    static class SubReactor implements Runnable {
        final Selector mySelector;
​
        //多线程处理业务逻辑
        int workCount =Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(workCount);
​
        ​
        public SubReactor() throws Exception {
            // 每个SubReactor 一个selector
            this.mySelector = SelectorProvider.provider().openSelector();
        }
​
        /**
         * 注册chanel
         *
         * @param sc
         * @throws Exception
         */
        public void registerChannel(SocketChannel sc) throws Exception {
            sc.register(mySelector, SelectionKey.OP_READ | SelectionKey.OP_CONNECT);
        }
​
        @Override
        public void run() {
            while (true) {
                try {
                    //每个SubReactor 自己做事件分派处理读写事件
                    mySelector.select();
                    Set<SelectionKey> keys = mySelector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()) {
                            read();
                        } else if (key.isWritable()) {
                            write();
                        }
                    }
​
                } catch (Exception e) {
​
                }
            }
        }
​
        private void read() {
            //任务异步处理
            executorService.submit(() -> process());
        }
​
        private void write() {
            //任务异步处理
            executorService.submit(() -> process());
        }
​
        /**
         * task 业务处理
         */
        public void process() {
            //do IO ,task,queue something
        }
    }
}
