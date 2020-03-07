/**
 * 
 */
package com.gupao.vip.zookeeper.javaapi;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 类名称：CreateSessionDemo<br>
 * 类描述：<br>
 * 创建时间：Mar 2, 20193:01:54 PM<br>
 *
 * @author Younger
 * @version	1.0.0
 */
public class CreateSessionDemo {

    private final static String CONNECTSTRING = "localhost:2281";
    private static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException,InterruptedException{
        ZooKeeper zooKeeper = new ZooKeeper(CONNECTSTRING,5000,new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println(event.getState()+"-->"+event.getType());
                if(event.getState() == Event.KeeperState.SyncConnected) {
                    latch.countDown();
                    System.out.println(event.getState()+"-->"+event.getType());
                }
            }
        });
        latch.await();
        System.out.println(zooKeeper.getState());
    }
}
