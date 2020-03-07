package com.younger.test.netty.chat.protocol;

public enum IMP {

    SYSTEM("SYSTEM"),
    LOGIN("LOGIN"),
    LOGOUT("LOGOUT"),
    CHAT("CHAT"),
    FLOWER("FLOWER");

    private String name;

    public static boolean isIMP(String content){
        return content.matches("^\\[(SYSTEM|LOGIN|LOGOUT|CHAT|FLOWER)\\]");
    }

    IMP(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "IMP{" +
                "name='" + name + '\'' +
                '}';
    }
}
