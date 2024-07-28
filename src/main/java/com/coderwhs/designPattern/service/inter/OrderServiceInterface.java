package com.coderwhs.designPattern.service.inter;

import com.coderwhs.designPattern.model.entity.Order;

import java.math.BigDecimal;

/**
 * @Author whs
 * @Date 2024/7/6 17:03
 * @description: 订单状态处理
 */
public interface OrderServiceInterface {

    /**
     * 创建订单
     * @param productId
     * @return
     */
    Order createOrder(String productId);

    /**
     * 支付订单
     * @param orderId
     * @return
     */
    Order payOrder(String orderId) throws Exception;

    /**
     * 订单发货
     * @param orderId
     * @return
     */
    Order sendOrder(String orderId) throws Exception;

    /**
     * 订单结束
     * @param orderId
     * @return
     */
    Order receiveOrder(String orderId) throws Exception;

    /**
     * 获取支付链接
     * @param orderId
     * @param price
     * @param payType
     * @return
     * @throws Exception
     */
    String getPayUrl(String orderId,BigDecimal price,Integer payType) throws Exception;

    void friendPay(String customerName, String orderId, String targetCustomer, String payResult,String role);
}
