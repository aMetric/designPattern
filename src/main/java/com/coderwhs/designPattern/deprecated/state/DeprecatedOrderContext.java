package com.coderwhs.designPattern.deprecated.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeprecatedOrderContext {

    @Autowired
    private DeprecatedCreateOrder createOrder;

    @Autowired
    private DeprecatedPayOrder payOrder;

    @Autowired
    private DeprecatedSendOrder sendOrder;

    @Autowired
    private DeprecatedReceiveOrder receiveOrder;


    public DeprecatedOrder createOrder(String orderId, String productId) {
        //创建订单，使用deprecatedCreateOrder
        return createOrder.createOrder(orderId, productId);
    }

    public DeprecatedOrder payOrder(String orderId) {
        //支付订单，使用deprecatedPayOrder
        return payOrder.payOrder(orderId);
    }

    public DeprecatedOrder sendOrder(String orderId) {
        //订单发货，使用 deprecatedSendOrder
        return sendOrder.sendOrder(orderId);
    }

    public DeprecatedOrder receiveOrder(String orderId) {
        //订单签收，使用 deprecatedReceiveOrder
        return receiveOrder.receiveOrder(orderId);
    }

}
