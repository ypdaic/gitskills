package com.daiyanping.cms.netty.splicing;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

public class  EchoClientHandle extends SimpleChannelInboundHandler<ByteBuf> {

    private AtomicInteger counter = new AtomicInteger(0);

    /**
     * 客户端读取到数据就会执行
     * @param channelHandlerContext
     * @param buffer
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf buffer) throws Exception {
        System.out.println("client accept:" + buffer.toString(CharsetUtil.UTF_8) + "and the counter is:" + counter.incrementAndGet());
    }

    /**
     * 连接建立后
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf msg = null;
        // System.getProperty("line.separator") 回车换行符
        String request = "Mark,Lison,Peter,James,Deer" + System.getProperty("line.separator");

        for (int i = 0; i < 10; i++) {
            msg = Unpooled.buffer(request.length());
            msg.writeBytes(request.getBytes());
            ctx.writeAndFlush(msg);

        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
