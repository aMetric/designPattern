package com.coderwhs.designPattern.deprecated.state;

import com.coderwhs.designPattern.common.ErrorCode;
import com.coderwhs.designPattern.exception.ThrowUtils;
import com.coderwhs.designPattern.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author whs
 * @Date 2024/6/30 10:20
 * @description: 具体状态类
 */
@Component
public class DeprecatedSendOrder extends DeprecatedAbstractOrderState {
    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    @Override
    protected DeprecatedOrder sendOrder(String orderId) {
        // 从redis中取出订单，判断当前订单是不是待发货状态
        DeprecatedOrder order  = (DeprecatedOrder)redisCommonProcessor.get(orderId);
        ThrowUtils.throwIf(!order.getState().equals(ORDER_WAIT_SEND), ErrorCode.PARAMS_ERROR,"订单状态应该为ORDER_WAIT_SEND，但当前是"+order.getState());

        //点击发货后，修改订单状态为待收货，并更新redis缓存
        order.setState(ORDER_WAIT_RECEIVE);
        redisCommonProcessor.set(orderId,order);

        //观察者模式。发送订单发货event
        super.notifyObserver(orderId,ORDER_WAIT_RECEIVE);

        return order;
    }
}
