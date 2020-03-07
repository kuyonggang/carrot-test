package com.younger.test.zookeeper.lock.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MasterSelector {

    private ZkClient zkClient;

    //需要争抢的节点
    private final static String MASTER_PATH = "/master";

    //注册节点内容变化
    private IZkDataListener dataListener;

    private UserCenter server;//其他服务器

    private UserCenter master;//master节点
    private boolean isRunning = false;
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    public MasterSelector(UserCenter server,ZkClient zkClient){
        System.out.println("["+server+"] 去争抢master权限");
        this.server = server;
        this.zkClient = zkClient;
        this.dataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {

            }
            @Override
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("触发节点删除事件！");
                //节点如果被删除，发起选主操作
                chooseMaster();
            }
        };
    }

    public void start(){
        //开始选举
        if(!isRunning){
            isRunning = true;
            //注册节点事件
            zkClient.subscribeDataChanges(MASTER_PATH,dataListener);
            chooseMaster();
        }
    }

    /**
     * 停止
     */
    public void stop(){
        if(isRunning){
            System.out.println("["+server+"] 停止！");
            isRunning = false;
            scheduledExecutorService.shutdown();
            zkClient.unsubscribeDataChanges(MASTER_PATH,dataListener);
            releaseMaster();
        }
    }

    private void chooseMaster(){
        if(!isRunning){
            System.out.println("当前服务没有启动");
            return;
        }
        try{
            zkClient.createEphemeral(MASTER_PATH,server);
            master = server;
            System.out.println("["+master+"] 我现在已经是master!");
            //模拟master释放锁，5秒后释放
            scheduledExecutorService.schedule(()->{
                releaseMaster();  //释放锁
            },4,TimeUnit.SECONDS);
        }catch(ZkNodeExistsException e){
            //表示master已经存在,再次获取master
            UserCenter userCenter = zkClient.readData(MASTER_PATH,true);
            if(userCenter == null){
                chooseMaster();
            }else{
                master = userCenter;
            }
        }
    }

    private void releaseMaster(){
        //释放锁（故障模拟过程）
        //判断当前是不是master,只有master才能释放
        if(checkIsMaster()){
            zkClient.deleteRecursive(MASTER_PATH);//删除
        }
    }

    private boolean checkIsMaster(){
        //判断当前的server是不是master
        UserCenter userCenter = zkClient.readData(MASTER_PATH,true);
        if(userCenter == null){
            return false;
        }
        if(userCenter.getMc_name().equals(server.getMc_name())){
            master = userCenter;
            return true;
        }
        return false;
    }
}
