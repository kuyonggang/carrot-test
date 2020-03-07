package com.younger.test.netty.tomcat.servlets;

import com.younger.test.netty.tomcat.http.AbstractServlet;
import com.younger.test.netty.tomcat.http.MyHttpRequest;
import com.younger.test.netty.tomcat.http.MyHttpResponse;

import java.io.UnsupportedEncodingException;

public class MyServlet extends AbstractServlet {
    @Override
    public void doGet(MyHttpRequest request, MyHttpResponse response) {
        try {
            response.write(request.getParameter("name"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(MyHttpRequest request, MyHttpResponse response) {

    }
}
