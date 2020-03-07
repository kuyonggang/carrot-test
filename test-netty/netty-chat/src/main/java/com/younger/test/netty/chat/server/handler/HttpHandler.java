package com.younger.test.netty.chat.server.handler;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    //classpath
    private URL baseURL = HttpHandler.class.getProtectionDomain().getCodeSource().getLocation();
    private final String WEB_ROOT = "webroot";

    private File getFileFromRoot(String fileName) throws URISyntaxException {
        String path = baseURL.toURI() + WEB_ROOT + "/" + fileName;
        if(path.startsWith("file:")){
            path = path.substring(6);
        }
        return new File(path);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        //获取客户端请求的url

        String uri = request.getUri();
        String page = uri.equals("/")?"chat.html":uri;
        RandomAccessFile file = new RandomAccessFile(getFileFromRoot(page),"r");
        System.out.println(file);
        String contextType = "text/html;";
        if(uri.endsWith(".css")){
            contextType = "text/css;";
        }else if(uri.endsWith(".js")){
            contextType = "text/javascript;";
        }else if(uri.toLowerCase().endsWith("(jpg|png|gif)$")){
            String ext = uri.substring(uri.lastIndexOf("."));
            contextType = "image/" + ext + ";";
        }
        HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE,contextType + "charset=utf-8;");
        boolean keepAlive = HttpHeaders.isKeepAlive(request);
        if(keepAlive){
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,file.length());
            response.headers().set(HttpHeaders.Names.CONNECTION,HttpHeaders.Values.KEEP_ALIVE);
        }

        ctx.write(response);
        ctx.write(new DefaultFileRegion(file.getChannel(),0,file.length()));
        ChannelFuture f = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if(!keepAlive){
            f.addListener(ChannelFutureListener.CLOSE);
        }
        file.close();
    }
}
