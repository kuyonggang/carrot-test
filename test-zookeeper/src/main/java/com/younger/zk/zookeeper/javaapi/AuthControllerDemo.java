package com.younger.zk.zookeeper.javaapi;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * acl（create/delete/admin/read/write）
 * 权限控制模式：
 * ip：192.168.2.1
 * Digest：username:password
 * world：开放式的权限控制模式，数据节点的访问权限对所有用户开放。world：anyone
 * super：超级用户，可以对zookeeper上的数据节点进行操作
 *
 */
public class AuthControllerDemo implements Watcher {

    private final static String CONNECT_STR = "localhost:2181";
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private static String schema = "digest";
    private static String username = "root";
    private static String password = "root";

    public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
        // 建立连接
        ZooKeeper zooKeeper = new ZooKeeper(CONNECT_STR,5000, new AuthControllerDemo());
        countDownLatch.await();
        zooKeeper.addAuthInfo("digest","root:root".getBytes());


//        zooKeeper.create("/auth","123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
//        zooKeeper.create("/auth","123".getBytes(), ZooDefs.Ids.READ_ACL_UNSAFE,CreateMode.PERSISTENT);

        ACL acl = new ACL(ZooDefs.Perms.ALL, new Id("digest","root:root"));
        List<ACL> acls = new ArrayList<ACL>();
        acls.add(acl);
        zooKeeper.create("/auth","123".getBytes(), acls, CreateMode.PERSISTENT);

        // 休眠10秒后删除该节点
        System.out.println("休眠10秒后删除该节点...");
        TimeUnit.SECONDS.sleep(10);
        ZooKeeper zookeeper1 = new ZooKeeper(CONNECT_STR,5000, new ApiOperatorDemo());
        zookeeper1.delete("/auth",-1);
    }


    public void process(WatchedEvent event) {
        //连接状态（event.getState()）：客户端和服务器端在某一个节点上建立连接，并且完成一次version、zxid同步
        if(event.getState() == Event.KeeperState.SyncConnected) {
            if(Event.EventType.None == event.getType() && null == event.getPath()) {
                countDownLatch.countDown();
                System.out.println(event.getState() + "-->" + event.getType());
            }
        }
    }
}

