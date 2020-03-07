package com.younger.test.zookeeper.lock.zkclient;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MasterChooseTest {

    private final static String CONNECT_STR = "localhost:2181";

    public static void main(String[] args) throws InterruptedException {
        List<MasterSelector> selectors = new ArrayList<>();
        try{
            for (int i = 0; i < 10; i++) {
                ZkClient zkClient = new ZkClient(CONNECT_STR,5000,5000,new SerializableSerializer());
                UserCenter userCenter = new UserCenter();
                userCenter.setMc_name("客户端："+i);
                MasterSelector masterSelector = new MasterSelector(userCenter,zkClient);
                masterSelector.start();
                selectors.add(masterSelector);
                TimeUnit.MILLISECONDS.sleep(200);
            }
            TimeUnit.SECONDS.sleep(20);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            for (MasterSelector selector:selectors) {
                selector.stop();
            }
        }

    }
}
