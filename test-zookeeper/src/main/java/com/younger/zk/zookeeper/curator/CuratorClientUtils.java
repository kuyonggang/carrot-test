package com.younger.zk.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorClientUtils {

    private final static String CONNECT_STR = "localhost:2181";
    private static CuratorFramework curatorFramework;

    public static CuratorFramework getCuratorFramework(){
        curatorFramework = CuratorFrameworkFactory
                .newClient(CONNECT_STR,5000,5000,
                        new ExponentialBackoffRetry(1000,3));
        curatorFramework.start();
        return curatorFramework;
    }

    public static CuratorFramework getCuratorFrameworkWithAuth(String schema,String signature){
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .authorization(schema,signature.getBytes())
                .connectString(CONNECT_STR).sessionTimeoutMs(5000).connectionTimeoutMs(5000)
//                .retryPolicy(new ExponentialBackoffRetry(1000,3)).namespace("curator").build();
                        .retryPolicy(new ExponentialBackoffRetry(1000,3)).build();
        curatorFramework.start();
        return curatorFramework;
    }
}
