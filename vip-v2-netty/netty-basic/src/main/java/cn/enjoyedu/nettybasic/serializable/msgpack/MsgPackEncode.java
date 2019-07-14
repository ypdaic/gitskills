package cn.enjoyedu.nettybasic.serializable.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @author Mark老师   享学课堂 https://enjoy.ke.qq.com
 * 类说明：
 */
public class MsgPackEncode extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
            throws Exception {
        MessagePack messagePack = new MessagePack();
        /*将对象编码为byte数组*/
        byte[] raw = messagePack.write(msg);
        out.writeBytes(raw);
    }
}
