package com.younger.zk.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.util.Collections;
import java.util.concurrent.TimeUnit;


public class AuthControllerDemo {

    private static String schema = "digest";
    private static String username = "root";
    private static String password = "root";
    private static final String COLON = ":";

    public static void main(String[] args) throws Exception {
        String signature = username + COLON + password;
        CuratorFramework curatorFramework = CuratorClientUtils.getCuratorFrameworkWithAuth(schema,signature);
        if(curatorFramework.checkExists().forPath("/user/younger") == null){
            curatorFramework.create().creatingParentsIfNeeded().forPath("/user/younger");
            System.out.println("创建节点/user/younger");
            signature = "zhangsan:zhangsan";
            curatorFramework.setACL().withACL(Collections.singletonList(new ACL(ZooDefs.Perms.ALL,new Id(schema,DigestAuthenticationProvider.generateDigest(signature))))).forPath("/user");
        }else{
            System.out.println("节点/user/younger已经存在");
        }

        System.out.println("5秒钟后删除/user/younger");
        TimeUnit.SECONDS.sleep(5);

        if (curatorFramework.checkExists().forPath("/user/younger") != null) {
            byte[] data = curatorFramework.getData().forPath("/user/younger");
            System.out.println(new String(data, "UTF-8"));
            //
//            curatorFramework.delete().deletingChildrenIfNeeded().forPath("/user");
            // 重新以一个新的签名获取zk客户端进行节点的删除操作，将会提示NoAuth for /user
//            signature = "zhangsan1:zhangsan1";
            signature = "root:root";
            curatorFramework = CuratorClientUtils.getCuratorFrameworkWithAuth(schema,signature);
            curatorFramework.delete().deletingChildrenIfNeeded().forPath("/user");
//        } else {
            System.out.println("节点/user/younger不存在");
        }
        TimeUnit.SECONDS.sleep(5);
        curatorFramework.close();
    }
}
