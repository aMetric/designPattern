package com.coderwhs.designPattern.deprecated.service.impl;

import com.coderwhs.designPattern.deprecated.service.DeprecatedOrderService;
import com.coderwhs.designPattern.deprecated.state.DeprecatedOrder;
import com.coderwhs.designPattern.deprecated.state.DeprecatedOrderContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author whs
 * @Date 2024/6/30 15:12
 * @description: 订单转化处理实现类
 */
@Service
public class DeprecatedOrderServiceImpl implements DeprecatedOrderService {

    @Autowired
    private DeprecatedOrderContext orderContext;

    /**
     * 创建订单
     *
     * @param productId
     * @return
     */
    @Override
    public DeprecatedOrder createOrder(String productId) {
        String orderId = "OID" + productId;
        return orderContext.createOrder(orderId,productId);
    }

    /**
     * 支付订单
     *
     * @param orderId
     * @return
     */
    @Override
    public DeprecatedOrder pay(String orderId) {
        return orderContext.payOrder(orderId);
    }

    /**
     * 订单发货
     *
     * @param orderId
     * @return
     */
    @Override
    public DeprecatedOrder send(String orderId) {
        return orderContext.sendOrder(orderId);
    }

    /**
     * 订单完成
     *
     * @param orderId
     * @return
     */
    @Override
    public DeprecatedOrder receive(String orderId) {
        return orderContext.receiveOrder(orderId);
    }
}
