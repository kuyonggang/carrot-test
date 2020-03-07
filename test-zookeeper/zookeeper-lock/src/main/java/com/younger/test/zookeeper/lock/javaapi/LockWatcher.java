package com.younger.test.zookeeper.lock.javaapi;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

public class LockWatcher implements Watcher {

    private CountDownLatch countDownLatch;

    public LockWatcher(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void process(WatchedEvent event) {
        if(event.getType() == Event.EventType.NodeDeleted){
            countDownLatch.countDown();
        }
    }
}
