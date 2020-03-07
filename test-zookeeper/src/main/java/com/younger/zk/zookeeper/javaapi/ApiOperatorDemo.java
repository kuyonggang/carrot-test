/**
 *
 */
package com.younger.zk.zookeeper.javaapi;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 类名称：ApiOperatorDemo<br>
 * 类描述：<br>
 * 创建时间：Mar 5, 20199:12:30 PM<br>
 *
 * @author Younger
 * @version	1.0.0
 */
public class ApiOperatorDemo implements Watcher{

        private static CountDownLatch latch = new CountDownLatch(1);
        private static CountDownLatch latch1 = new CountDownLatch(1);
        private static String connectString = "localhost:2181";
        private static ZooKeeper zookeeper;
        private static Stat stat = new Stat();

        public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
            zookeeper = new ZooKeeper(connectString, 5000, new ApiOperatorDemo());
            latch.await();
            zookeeper.addAuthInfo("digest", "root:root".getBytes());
            zookeeper.create("/auth", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            ZooKeeper zookeeper1 = new ZooKeeper(connectString, 5000, new ApiOperatorDemo());
            latch1.await();
            zookeeper1.delete("/auth", -1);

        }

        /* (non-Javadoc)
         * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
         */
        public void process(WatchedEvent event) {
            if(event.getState() == Event.KeeperState.SyncConnected) {
                if(Event.EventType.None == event.getType() && null == event.getPath()) {
                    latch.countDown();
                    latch1.countDown();
                    System.out.println(event.getState() + "-->" + event.getType());
                }
            }
        }
}
