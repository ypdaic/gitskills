package com.daiyanping.cms.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import javax.persistence.criteria.CriteriaBuilder;
import java.net.InetSocketAddress;

/**
 * netty 客户端
 */
public class EchoClient {

    private final int port;

    private final String host;

    public EchoClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public void start() throws InterruptedException {
        /**
         * 线程组
         */
        EventLoopGroup group = new NioEventLoopGroup();

        /**
         * 客户端启动必备
         */
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group);
        // 指定使用NIO进行网络传输
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.remoteAddress(new InetSocketAddress(this.host, this.port));
        EchoClientHandle echoClientHandle = new EchoClientHandle();
        bootstrap.handler(echoClientHandle);

        /**
         * 连接到远程节点，直到连接完成
         */
        ChannelFuture sync = bootstrap.connect().sync();
        /**
         * 阻塞直到channel方法发生了关闭
         */
        sync.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoClient(9999, "127.0.0.1"). start();
    }

}
