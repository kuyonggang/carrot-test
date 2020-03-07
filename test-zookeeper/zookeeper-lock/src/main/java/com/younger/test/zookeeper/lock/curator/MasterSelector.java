package com.younger.test.zookeeper.lock.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

public class MasterSelector {

    private final static String CONNECT_STR = "localhost:2181";
    private final static String MASTER_PATH = "/curator_master_path";

    public static void main(String[] args){
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString(CONNECT_STR)
                .retryPolicy(new ExponentialBackoffRetry(1000,3)).build();

        LeaderSelector leaderSelector = new LeaderSelector(curatorFramework, MASTER_PATH, new LeaderSelectorListener() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                System.out.println("获得leader成功");
                TimeUnit.SECONDS.sleep(2);
            }

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {

            }
        });
        leaderSelector.autoRequeue();
        leaderSelector.start();
    }
}
