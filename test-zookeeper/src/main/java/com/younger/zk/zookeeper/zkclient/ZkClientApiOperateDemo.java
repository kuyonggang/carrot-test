package com.younger.zk.zookeeper.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ZkClientApiOperateDemo {

    private final static String CONNECTSTRING = "localhost:2181";
    
    private static ZkClient getInstance() {
        ZkClient zkClient = new ZkClient(CONNECTSTRING, 3000);
        return zkClient;
    }

    public static void main(String[] args) throws InterruptedException {

        ZkClient zkClient = getInstance();
//        zkClient.createEphemeral("/auth11");
//        zkClient 提供递归创建父节点的功能
//        zkClient.createPersistent("/hello/name/zhangsan", true);
         //递归删除节点
//        zkClient.deleteRecursive("/hello");

        //获取子节点
        List<String> list = zkClient.getChildren("/hello");
        for (String node : list) {
            System.out.println(node);
        }
        System.out.println("success");
        
        //watcher
        zkClient.subscribeDataChanges("/hello", new IZkDataListener() {
            
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("节点删除："+dataPath);
            }
            
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("节点名称："+s+" ->节点修改后的值："+o);
            }
        });
        zkClient.writeData("/hello", "王五");
        TimeUnit.SECONDS.sleep(2);
    }

}
