package com.younger.test.netty.chat.server.handler;

import com.younger.test.netty.chat.process.IMProcessor;
import com.younger.test.netty.chat.protocol.IMMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SocketHandler extends SimpleChannelInboundHandler<IMMessage> {

    private IMProcessor processor = new IMProcessor();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMMessage msg) throws Exception {
        processor.process(ctx.channel(),msg);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有客户端链接");
    }
}
