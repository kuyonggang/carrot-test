package com.younger.zk.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorCreateSessionDemo {

    private final static String CONNECT_STR = "localhost:2181";

    public static void main(String[] args) throws Exception {
        //创建回话的两种方式
        //normal
        CuratorFramework curatorFramework = CuratorFrameworkFactory
                .newClient(CONNECT_STR,5000,5000,
                        new ExponentialBackoffRetry(1000,3));
        curatorFramework.start();//start方法启动链接

        //fluent风格
        CuratorFramework curatorFramework1 = CuratorFrameworkFactory.builder().connectString(CONNECT_STR).sessionTimeoutMs(5000).connectionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3)).namespace("curator").build();
        curatorFramework1.start();

        //创建的节点将会是"/curator/aaa"
        curatorFramework1.createContainers("/aaa");

        System.out.println("success");
    }

}
