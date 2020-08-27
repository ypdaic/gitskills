package com.daiyanping.cms.netty.fixed;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;

import java.net.InetSocketAddress;

/**
 * netty 客户端
 */
public class EchoClient {

    // 固定长度的消息
    public static final String MSG = "Mark,Lison,Peter,James,Deer";

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
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                // 固定长度的handle，服务端定义的长度
                ch.pipeline().addLast(new FixedLengthFrameDecoder(EchoServer.MSG.length()));
                ch.pipeline().addLast(echoClientHandle);
            }
        });

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
