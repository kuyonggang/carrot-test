package com.younger.test.netty.rpc.provider;

import com.younger.test.netty.rpc.api.IRpcCal;

public class RpcCal implements IRpcCal {

    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int sub(int a, int b) {
        return a - b;
    }
}
