package com.younger.test.netty.chat.server;

import com.younger.test.netty.chat.protocol.IMDecoder;
import com.younger.test.netty.chat.protocol.IMEncoder;
import com.younger.test.netty.chat.server.handler.HttpHandler;
import com.younger.test.netty.chat.server.handler.SocketHandler;
import com.younger.test.netty.chat.server.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ChatServer {

    public void start(int port)throws Exception {
        //Boss线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //Worker线程
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //Netty服务
            ServerBootstrap server = new ServerBootstrap();
            //主从模型
            server.group(bossGroup, workerGroup)
                    //主线程处理类
                    .channel(NioServerSocketChannel.class)
                    //子线程处理类， Handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel client) throws Exception {
                            //所有自定义的业务从这里开始
                            ChannelPipeline pipeline = client.pipeline();

                            /**
                             * 支持自定义socket协议
                             */
                            pipeline.addLast(new IMDecoder());
                            pipeline.addLast(new IMEncoder());
                            pipeline.addLast(new SocketHandler());

                            /**
                             * 这里它是用来支持HTTP协议
                             */
                            //解码和编码HTTP请求
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(64 * 1024));
                            //用户处理文件流的handler
                            pipeline.addLast(new ChunkedWriteHandler());
                            pipeline.addLast(new HttpHandler());

                            /**
                             * 支持WebSocket协议
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/im"));
                            pipeline.addLast(new WebSocketHandler());
                        }
                    })
                    //配置信息
                    .option(ChannelOption.SO_BACKLOG, 128) //针对主线程的配置
                    .childOption(ChannelOption.SO_KEEPALIVE, true); //针对子线程的配置
            //等待客户端连接
            ChannelFuture f = server.bind(port).sync();
            System.out.println("Tomcat已经启动:" + port);
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        try{
            new ChatServer().start(80);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
