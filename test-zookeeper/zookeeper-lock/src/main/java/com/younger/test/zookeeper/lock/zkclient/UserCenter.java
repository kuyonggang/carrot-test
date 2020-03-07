package com.younger.test.zookeeper.lock.zkclient;

import java.io.Serializable;

public class UserCenter implements Serializable {

    private String mc_id;
    private String mc_name;

    public String getMc_id() {
        return mc_id;
    }

    public void setMc_id(String mc_id) {
        this.mc_id = mc_id;
    }

    public String getMc_name() {
        return mc_name;
    }

    public void setMc_name(String mc_name) {
        this.mc_name = mc_name;
    }

    @Override
    public String toString() {
        return "UserCenter{" +
                "mc_id='" + mc_id + '\'' +
                ", mc_name='" + mc_name + '\'' +
                '}';
    }
}
