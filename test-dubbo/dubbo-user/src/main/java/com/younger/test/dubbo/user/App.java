package com.younger.test.dubbo.user;

import com.younger.test.dubbo.order.api.common.DoOrderRequest;
import com.younger.test.dubbo.order.api.common.DoOrderResponse;
import com.younger.test.dubbo.order.api.service.IOrderService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("order-comsumer.xml");

        IOrderService orderService = (IOrderService) context.getBean("orderService");
        DoOrderRequest request = new DoOrderRequest();
        request.setName("mic");
        DoOrderResponse response = orderService.doOrder(request);

        System.out.println(response);

    }
}
