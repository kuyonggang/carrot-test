package com.younger.test.zookeeper.lock.javaapi;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

public class DistributeLock {

    private static final String ROOT_LOCKS = "/LOCKS";//根节点

    private ZooKeeper zooKeeper;

    //会话超时时间
    private int sessionTimeout;

    //记录锁节点ID
    private String lockId;

    //节点的数据
    private final static byte[] data = {1,2};

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public DistributeLock() throws IOException, InterruptedException, KeeperException {
        this.zooKeeper = ZookeeperClient.getInstance();
        this.sessionTimeout = ZookeeperClient.getSessionTimeout();
    }

    public boolean lock() throws KeeperException, InterruptedException {
        lockId = zooKeeper.create(ROOT_LOCKS+"/",data,ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(Thread.currentThread().getName()+"->成功创建了lock节点["+ lockId +"],开始去竞争锁");
        List<String> childrenNodes = zooKeeper.getChildren(ROOT_LOCKS,true);
        SortedSet<String> sortedSet = new TreeSet<>();
        for (String child:childrenNodes) {
            sortedSet.add(ROOT_LOCKS + "/" + child);
        }
        String first = sortedSet.first();
        if(lockId.equals(first)){
            // 表示当前就是最小的节点
            System.out.println(Thread.currentThread().getName()+"->成功获得锁，lock节点为：[" + lockId + "]");
            return true;
        }
        SortedSet<String> lessThanLockId = ((TreeSet<String>) sortedSet).headSet(lockId);
        if(!lessThanLockId.isEmpty()){
            //拿到比当前LockId这个节点更小的上一个节点
            String prevLockId = lessThanLockId.last();
            zooKeeper.exists(prevLockId,new LockWatcher(countDownLatch));
            countDownLatch.await();
            //上面这段代码意味着如果会话超时或者节点被删除了
            System.out.println(Thread.currentThread().getName()+" 成功获得锁：["+lockId+"]");
        }
        return true;
    }

    public boolean unlock(){
        System.out.println(Thread.currentThread().getName()+"->开始释放锁：[" + lockId + "]");
        try{
            zooKeeper.delete(lockId,-1);
            System.out.println("节点[" + lockId + "]成功被删除！");
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        final CountDownLatch countDownLatch = new CountDownLatch(10);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                DistributeLock lock = null;
                try {
                    lock = new DistributeLock();
                    countDownLatch.countDown();
                    countDownLatch.await();
                    lock.lock();
                    Thread.sleep(random.nextInt(5000));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally{
                    if(lock!=null){
                        lock.unlock();
                    }
                }
            }).start();
        }
    }

}
