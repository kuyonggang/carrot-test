package com.younger.test.dubbo.order.provider;

import com.younger.test.dubbo.order.api.common.DoOrderRequest;
import com.younger.test.dubbo.order.api.common.DoOrderResponse;
import com.younger.test.dubbo.order.api.service.IOrderService;

public class OrderServiceImpl implements IOrderService {
    @Override
    public DoOrderResponse doOrder(DoOrderRequest request) {

        DoOrderResponse response = new DoOrderResponse();
        response.setCode("11111");
        response.setMemo("到此一游");
        return response;
    }
}
