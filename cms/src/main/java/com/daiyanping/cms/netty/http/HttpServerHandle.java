package com.daiyanping.cms.netty.http;

import com.google.gson.internal.$Gson$Preconditions;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 粘包，半包
 */
@ChannelHandler.Sharable
public class HttpServerHandle extends ChannelInboundHandlerAdapter {

    /*
     * 建立连接时，返回消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接的客户端地址:" + ctx.channel().remoteAddress());
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
        String result="";
        FullHttpRequest httpRequest = (FullHttpRequest)msg;
        System.out.println(httpRequest.headers());
        try{
            //获取路径
            String path=httpRequest.uri();
            //获取body
            String body = httpRequest.content().toString(CharsetUtil.UTF_8);
            //获取请求方法
            HttpMethod method=httpRequest.method();
            //如果不是这个路径，就直接返回错误
            if(!"/test".equalsIgnoreCase(path)){
                result="非法请求!"+path;
                send(ctx,result,HttpResponseStatus.BAD_REQUEST);
                return;
            }
            System.out.println("接收到:"+method+" 请求");
            //如果是GET请求
            if(HttpMethod.GET.equals(method)){
                //接受到的消息，做业务逻辑处理...
                System.out.println("body:"+body);
                result="GET请求,应答:"+ "xxxxxxxxxxx";
                send(ctx,result,HttpResponseStatus.OK);
                return;
            }
            //如果是其他类型请求，如post
            if(HttpMethod.POST.equals(method)){
                //接受到的消息，做业务逻辑处理...
                //....
                return;
            }

        }catch(Exception e){
            System.out.println("处理请求失败!");
            e.printStackTrace();
        }finally{
            //释放请求
            httpRequest.release();
        }
    }

    private void send(ChannelHandlerContext ctx, String result, HttpResponseStatus status) {
        DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer(result, CharsetUtil.UTF_8));
        defaultFullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(defaultFullHttpResponse).addListener(ChannelFutureListener.CLOSE);
    }


    /**
     * 服务端读取完成网络数据后的处理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();
    }
}
