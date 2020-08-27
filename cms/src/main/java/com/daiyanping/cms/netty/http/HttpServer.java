package com.daiyanping.cms.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.net.InetSocketAddress;

/**
 * netty 客户端
 */
public class HttpServer {

    // 固定长度的消息
    public static final String MSG = "Welcome to Netty Word!";

    private final int port;

    private final String host;

    private final boolean SSL = true;

    public HttpServer(int port, String host) {
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
            HttpServerHandle echoServerHandle = new HttpServerHandle();

            /**
             * 可以传入多个handle
             */
//        bootstrap.childHandler(echoServerHandle);
            final SslContext sslCtx ;
            if(SSL){
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(),
                        ssc.privateKey()).build();
            }else{
                sslCtx = null;
            }
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // https 支持
                    if(sslCtx != null){
                        ch.pipeline().addLast(sslCtx.newHandler(ch.alloc()));
                    }
                    // 处理http服务的关键handler
//                    ch.pipeline().addLast("encoder", new HttpResponseEncoder());
//                    ch.pipeline().addLast("decoder", new HttpRequestDecoder());
                    ch.pipeline().addLast("decoder", new HttpServerCodec());
                    ch.pipeline().addLast("aggregator", new HttpObjectAggregator(10 *1024 * 1024));
                    // 压缩支持
                    ch.pipeline().addLast("compressor",new HttpContentCompressor());
                    ch.pipeline().addLast(echoServerHandle);
                }
            });

            // 绑定端口
            ChannelFuture sync = bootstrap.bind().sync();
            // 阻塞直到连接 关闭
            sync.channel().closeFuture().sync();
        } catch (Exception e) {

        }
        finally {
            group.shutdownGracefully();
        }



    }

    public static void main(String[] args) throws InterruptedException {
        new HttpServer(9999, "127.0.0.1"). start();
    }

}
