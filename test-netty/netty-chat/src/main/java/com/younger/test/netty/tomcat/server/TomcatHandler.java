package com.younger.test.netty.tomcat.server;

import com.younger.test.netty.tomcat.http.MyHttpRequest;
import com.younger.test.netty.tomcat.http.MyHttpResponse;
import com.younger.test.netty.tomcat.servlets.MyServlet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

public class TomcatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest){
            HttpRequest r = (HttpRequest) msg;
            MyHttpRequest request = new MyHttpRequest(ctx,r);
            MyHttpResponse response = new MyHttpResponse(ctx,r);

            new MyServlet().doGet(request,response);

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
