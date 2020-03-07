package com.younger.test.netty.rpc.consumer;

import com.younger.test.netty.rpc.api.IRpcCal;
import com.younger.test.netty.rpc.api.IRpcHello;
import com.younger.test.netty.rpc.consumer.proxy.RpcProxy;

public class RpcConsumer{

    public static void main(String[] args) {
        IRpcHello rpcHello = RpcProxy.create(IRpcHello.class);
        String result = rpcHello.hello("zhangsan");
        System.out.println("调用结果:" + result);

        IRpcCal rpcCal = RpcProxy.create(IRpcCal.class);
        System.out.println(rpcCal.add(3,4));
        System.out.println(rpcCal.sub(5,2));
    }
}
