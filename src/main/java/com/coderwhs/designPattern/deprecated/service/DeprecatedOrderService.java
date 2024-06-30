package com.coderwhs.designPattern.deprecated.service;

import com.coderwhs.designPattern.deprecated.state.DeprecatedOrder;

/**
 * @Author whs
 * @Date 2024/6/30 15:12
 * @description:  订单转化处理接口
 */
public interface DeprecatedOrderService {
    /**
     * 创建订单
     * @param productId
     * @return
     */
    DeprecatedOrder createOrder(String productId);

    /**
     * 支付订单
     * @param orderId
     * @return
     */
    DeprecatedOrder pay(String orderId);

    /**
     * 订单发货
     * @param orderId
     * @return
     */
    DeprecatedOrder send(String orderId);

    /**
     * 订单完成
     * @param orderId
     * @return
     */
    DeprecatedOrder receive(String orderId);
}
