package com.daiyanping.cms.netty.fixed;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

import static com.daiyanping.cms.netty.delimiter.EchoServer.DELIMITER_SYMBOL;

/**
 * 粘包，半包
 */
@ChannelHandler.Sharable
public class EchoServerHandle extends ChannelInboundHandlerAdapter {

    private AtomicInteger counter = new AtomicInteger(0);

    private AtomicInteger completeCounter = new AtomicInteger(0);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    /**
     * 客户端读到数据以后，就会执行，这并没有打印一百次，而是一次，出现粘包问题
     * 解决方案：分隔符，固定长度，带长度
     *
     * 这里的触发读取就是我们业务的一个完整的报文接受后才会触发
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        String request = in.toString(CharsetUtil.UTF_8);
        System.out.println("Server Accept[" + request + "] and the counter is:" + counter.incrementAndGet());

        ctx.writeAndFlush(Unpooled.copiedBuffer(EchoServer.MSG.getBytes()));
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
        ctx.fireChannelReadComplete();
        /**
         * 只打印了一次，也就是读取完客户端发送的数据，不会管这个数据是否是一个完整报文，还是几个完整的报文
         */
        System.out.println("the ReadComplete count is " + completeCounter.incrementAndGet());
    }
}
