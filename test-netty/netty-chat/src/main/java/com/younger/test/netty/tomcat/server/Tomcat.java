package com.younger.test.netty.tomcat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class Tomcat {

    public void start(int port)throws Exception {

//        ServerSocketChannel s = ServerSocketChannel.open();
//        s.bind(local);
//
//        ServerSocket serverSocket = new ServerSocket(port);

            // 主从模型
            //Boss线程
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            //Worker线程
            EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //Netty服务
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup)
                    //主线程处理类
                    .channel(NioServerSocketChannel.class)
                    //子线程处理类， Handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel client) throws Exception {
                            //业务逻辑链路，编码器
                            client.pipeline().addLast(new HttpResponseEncoder());
                            //解码器
                            client.pipeline().addLast(new HttpRequestDecoder());
                            //业务逻辑处理
                            client.pipeline().addLast(new TomcatHandler());
                        }
                    })
                    //配置信息
                    .option(ChannelOption.SO_BACKLOG, 128) //针对主线程的配置
                    .childOption(ChannelOption.SO_KEEPALIVE, true); //针对子线程的配置

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
            new Tomcat().start(8080);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
