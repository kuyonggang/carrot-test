package com.younger.test.netty.tomcat.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

public class MyHttpRequest {

    private ChannelHandlerContext context;
    private HttpRequest request;

    public MyHttpRequest(ChannelHandlerContext context,HttpRequest request) {
        this.context = context;
        this.request = request;
    }

    public String getUrl(){
        return request.getUri();
    }

    public String getMethod(){
        return request.getClass().getName();
    }

    public Map<String,List<String>> getParameter(){
        QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
        return decoder.parameters();
    }

    public String getParameter(String name){
        QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
        Map<String,List<String>> params = getParameter();
        List<String> param = params.get(name);
        if(param == null){
            return null;
        }else{
            return param.get(0);
        }
    }
}
