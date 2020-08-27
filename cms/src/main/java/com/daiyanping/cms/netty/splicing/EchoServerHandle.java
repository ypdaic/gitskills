package com.daiyanping.cms.netty.splicing;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 粘包，半包
 */
@ChannelHandler.Sharable
public class EchoServerHandle extends ChannelInboundHandlerAdapter {

    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    /**
     * 客户端读到数据以后，就会执行，这并没有打印一百次，而是一次，出现粘包问题
     * 解决方案：分隔符，固定长度，带长度
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        String request = in.toString(CharsetUtil.UTF_8);
        System.out.println("Server Accept[" + request + "] and the counter is:" + counter.incrementAndGet());
        String resp = "Hello, " + request + ". Welcome to Netty Word!" + System.getProperty("line.separator");

        ctx.writeAndFlush(Unpooled.copiedBuffer(resp.getBytes()));
        // 手动释放ByteBuf，针对读数据
//        ReferenceCountUtil.release(msg);
    }

    /**
     * 服务端读取完成网络数据后的处理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将数据刷到对端，而不用发送正在的数据
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
}
