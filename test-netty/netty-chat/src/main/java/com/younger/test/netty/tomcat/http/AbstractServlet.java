package com.younger.test.netty.tomcat.http;

public abstract class AbstractServlet {

    public abstract void doGet(MyHttpRequest request,MyHttpResponse response);
    public abstract void doPost(MyHttpRequest request,MyHttpResponse response);
}
