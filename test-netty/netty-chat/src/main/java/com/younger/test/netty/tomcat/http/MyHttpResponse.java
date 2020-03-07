package com.younger.test.netty.tomcat.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;

import static io.netty.handler.codec.http.HttpHeaderNames.*;

public class MyHttpResponse {

    private ChannelHandlerContext context;
    private HttpRequest request;

    public MyHttpResponse(ChannelHandlerContext context, HttpRequest request) {
        this.context = context;
        this.request = request;
    }

    public void write(String out) throws UnsupportedEncodingException {
        try{
            if(out == null){
                return ;
            }
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(out.getBytes("UTF-8")));
            response.headers().set(CONTENT_TYPE,"text/json");
            response.headers().set(CONTENT_LENGTH,response.content().array().length);
            response.headers().set(EXPIRES,0);
            if(HttpHeaders.isKeepAlive(response)){
                response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            context.write(response);
        }finally {
            context.flush();
        }
    }
}
