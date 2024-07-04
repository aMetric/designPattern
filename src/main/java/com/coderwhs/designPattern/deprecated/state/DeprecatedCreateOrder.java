package com.coderwhs.designPattern.deprecated.state;

import com.coderwhs.designPattern.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author whs
 * @Date 2024/6/30 10:16
 * @description: 具体状态类
 */
@Component
public class DeprecatedCreateOrder extends DeprecatedAbstractOrderState {

    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    /**
     * 订单方法-创建订单
     * @param orderId
     * @param productId
     * @return
     */
    @Override
    protected DeprecatedOrder createOrder(String orderId, String productId) {
        //订单创建成功后，将订单状态设置为待支付
        DeprecatedOrder order = DeprecatedOrder.builder()
                .orderId(orderId)
                .productId(productId)
                .state(ORDER_WAIT_PAY)
                .build();

        //将订单存入redis，有效时间15分钟
        redisCommonProcessor.set(orderId,order,900);

        //观察者模式，发送订单创建event
        super.notifyObserver(orderId,ORDER_WAIT_PAY);

        return order;
    }
}
