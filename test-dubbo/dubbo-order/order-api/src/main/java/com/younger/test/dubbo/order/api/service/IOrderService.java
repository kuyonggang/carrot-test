package com.younger.test.dubbo.order.api.service;

import com.younger.test.dubbo.order.api.common.DoOrderRequest;
import com.younger.test.dubbo.order.api.common.DoOrderResponse;

public interface IOrderService {

    /**
     * 下单操作
     * @param request
     * @return
     */
    DoOrderResponse doOrder(DoOrderRequest request);
}
