package com.daiyanping.cms.netty.fixed;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;

import java.net.InetSocketAddress;

/**
 * netty 客户端
 */
public class EchoServer {

    // 固定长度的消息
    public static final String MSG = "Welcome to Netty Word!";

    private final int port;

    private final String host;

    public EchoServer(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public void start() throws InterruptedException {
        /**
         * 线程组
         */
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            /**
             * 服务端启动必备
             */
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group);
            // 指定使用NIO进行网络传输
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.localAddress(new InetSocketAddress(this.host, this.port));

            // 多个客户端共用一个handle的话，需要加上@ChannelHandler.Sharable注解
            EchoServerHandle echoServerHandle = new EchoServerHandle();

            /**
             * 可以传入多个handle
             */
//        bootstrap.childHandler(echoServerHandle);

            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 固定长度的handle，客户端定义的长度
                    ch.pipeline().addLast(new FixedLengthFrameDecoder(EchoClient.MSG.length()));
                    ch.pipeline().addLast(echoServerHandle);
                }
            });

            // 绑定端口
            ChannelFuture sync = bootstrap.bind().sync();
            // 阻塞直到连接 关闭
            sync.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }



    }

    public static void main(String[] args) throws InterruptedException {
        new EchoServer(9999, "127.0.0.1"). start();
    }

}