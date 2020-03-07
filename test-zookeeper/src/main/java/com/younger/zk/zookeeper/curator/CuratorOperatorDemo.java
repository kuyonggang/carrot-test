package com.younger.zk.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;

import java.util.Collection;

public class CuratorOperatorDemo {

    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = CuratorClientUtils.getCuratorFramework();
        System.out.println("链接成功...");

        // 创建节点
//        String result = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
//                    .forPath("/curator/curator1/curator11","123".getBytes());
//        System.out.println(result);

        // 删除节点
//        curatorFramework.delete().deletingChildrenIfNeeded().forPath("/curator/curator1");

        // 查询数据
//        Stat stat = new Stat();
//        byte[] bytes = curatorFramework.getData().storingStatIn(stat).forPath("/curator");
//        System.out.println(new String(bytes)+"-->stat:"+stat);

        // 修改数据
//        Stat stat = curatorFramework.setData().forPath("/curator","123".getBytes());
//        System.out.println(stat);

        // 异步操作
//        ExecutorService service = Executors.newFixedThreadPool(1);
//        final CountDownLatch latch = new CountDownLatch(1);
//        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
//                .inBackground(new BackgroundCallback() {
//                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
//                        System.out.println(Thread.currentThread().getName()+"->resultCode:"+event.getResultCode()+"->"+event.getType());
//                        latch.countDown();
//                    }
//                },service).forPath("/mic","234".getBytes());
//        latch.await();
//        service.shutdown();

        // 事务操作
        Collection<CuratorTransactionResult> resultCollections = curatorFramework.inTransaction()
                .create().forPath("/trans","111".getBytes()).and()
                //如果/xxx不存在那么setData就会出错，也将不会创建："/trans"
//                .setData().forPath("/xxxx","111".getBytes()).and().commit();
                .setData().forPath("/curator","111".getBytes()).and().commit();
        for (CuratorTransactionResult result: resultCollections) {
            System.out.println(result.getForPath() + "->" + result.getType());
        }
    }
}
