package com.younger.test.io.bio;

import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

public class BIOClient2 {

    public static void main(String[] args) {
        try {
            //开一条乡村公路
            Socket client = new Socket("localhost",8080);
            //输出流通道打开
            OutputStream os = client.getOutputStream();
            //产生一个随机的字符串，UUID
            String name = UUID.randomUUID().toString();
            System.out.println("send:"+name);
            //发送给服务端
            os.write(name.getBytes());
            os.close();
            client.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
