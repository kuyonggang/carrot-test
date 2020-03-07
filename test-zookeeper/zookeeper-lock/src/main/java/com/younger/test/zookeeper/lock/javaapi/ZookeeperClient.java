package com.younger.test.zookeeper.lock.javaapi;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperClient {

    private final static String CONNECT_STR = "localhost:2181";

    private final static Integer sessionTimeout = 5000;

    public static ZooKeeper getInstance() throws IOException, InterruptedException {

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper(CONNECT_STR, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if(event.getState() == Event.KeeperState.SyncConnected && event.getType() == Event.EventType.None){
                    countDownLatch.countDown();
                    System.out.println("连接zk成功！！！");
                }
            }
        });
        countDownLatch.await();
        return zooKeeper;
    }

    public static Integer getSessionTimeout() {
        return sessionTimeout;
    }
}
