package com.younger.test.netty.chat.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 自定义IM协议解码器
 */
public class IMDecoder extends ByteToMessageDecoder {

    private Pattern pattern = Pattern.compile("^\\[(.*)\\](\\s\\-\\s(.*))?");

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        try {
            final int length = in.readableBytes();
            final byte[] array = new byte[length];

            //变成一个字符串
            String content = new String(array,in.readableBytes(),length);
            if(!(content == null || "".equals(content.trim()))){
                if(!IMP.isIMP(content)){
                    ctx.channel().pipeline().remove(this);
                    return;
                }
            }

            in.getBytes(in.readableBytes(),array,0,length);
            //首先将字节转为MessagePack，然后再把这个Msg转为IMMessage
//            out.add(new MessagePack().read(array,IMMessage.class));
            in.clear();
        } catch (Exception e) {
            ctx.channel().pipeline().remove(this);
            e.printStackTrace();
        }
    }
}
