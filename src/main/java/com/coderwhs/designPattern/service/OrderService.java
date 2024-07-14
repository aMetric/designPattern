package com.coderwhs.designPattern.service;

import com.alipay.api.AlipayApiException;
import com.coderwhs.designPattern.common.BaseResponse;
import com.coderwhs.designPattern.model.entity.Order;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @Author whs
 * @Date 2024/7/6 17:03
 * @description: 订单状态处理
 */
public interface OrderService {

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
     * 支付回调接口
     * @param request
     * @return
     */
    String alipayCallback(HttpServletRequest request) throws Exception;

    /**
     * 获取支付链接
     * @param orderId
     * @param price
     * @param payType
     * @return
     * @throws Exception
     */
    String getPayUrl(String orderId,BigDecimal price,Integer payType) throws Exception;
}
