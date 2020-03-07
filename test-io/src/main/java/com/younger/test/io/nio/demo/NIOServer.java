package com.younger.test.io.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    private int port;
    private Selector selector;
    private InetSocketAddress address;

    public NIOServer(int port){
        try{
            this.port = port;
            address = new InetSocketAddress(this.port);
            //创建通道
            ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(address);            //默认为阻塞，需要手动设置为非阻塞
            server.configureBlocking(false);

            //Option的简称
            selector = Selector.open();

            //OPtion的简称
            server.register(selector,SelectionKey.OP_ACCEPT);
            System.out.println("服务器准备就绪，监听端口是：" + this.port);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void listen(){
        //轮询
        while(true){
            try{
                //多少人在服务大厅排队
                int wait = this.selector.select();
                if(wait == 0){
                    continue;
                }
                Set<SelectionKey> keys = this.selector.selectedKeys();
                Iterator<SelectionKey> i = keys.iterator();
                while(i.hasNext()){
                    SelectionKey key = i.next();
                    process(key);
                    i.remove();
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }

    private void process(SelectionKey key) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        if(key.isAcceptable()){
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            client.register(selector,SelectionKey.OP_READ);
        }else if(key.isReadable()){
            SocketChannel client = (SocketChannel) key.channel();
            int len = client.read(byteBuffer);
            if(len > 0){
                byteBuffer.flip();
                String content = new String(byteBuffer.array(),0,len);
                System.out.println(content);
                client.register(selector,SelectionKey.OP_WRITE);
            }
            byteBuffer.clear();
            client.finishConnect();
        }else if(key.isWritable()){
            SocketChannel client = (SocketChannel) key.channel();
            client.write(byteBuffer.wrap("Hello World".getBytes()));
            client.close();
        }else if(key.isValid()){

        }
    }

    public static void main(String[] args) {
        new NIOServer(8080).listen();
    }
}
