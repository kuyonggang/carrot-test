package com.younger.test.netty.chat.process;

import com.younger.test.netty.chat.protocol.IMMessage;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class IMProcessor {

    private final static ChannelGroup onlineUsers = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public void process(Channel client,String msg){

    }

    public void process(Channel client, IMMessage msg){
        process(client,"");
    }
}
