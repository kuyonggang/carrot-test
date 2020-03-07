package com.younger.test.netty.rpc.provider;

import com.younger.test.netty.rpc.api.IRpcHello;

public class RpcHello implements IRpcHello {

    @Override
    public String hello(String name) {
        return "Hello," + name + " !";
    }
}
