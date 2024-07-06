package com.coderwhs.designPattern.service.impl;

import com.coderwhs.designPattern.model.entity.Order;
import com.coderwhs.designPattern.model.enums.OrderStateChangeActionEnum;
import com.coderwhs.designPattern.model.enums.OrderStateEnum;
import com.coderwhs.designPattern.service.OrderService;
import com.coderwhs.designPattern.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

/**
 * @Author whs
 * @Date 2024/7/6 17:07
 * @description: 订单状态处理
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StateMachine<OrderStateEnum, OrderStateChangeActionEnum> orderStateMachine;

    //持久化状态机
    @Autowired
    private StateMachinePersister<OrderStateEnum, OrderStateChangeActionEnum,String> stateMachinePersister;

    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    /**
     * 创建订单
     *
     * @param productId
     * @return
     */
    @Override
    public Order createOrder(String productId) {
        String orderId = "OID" + productId;
        Order order = Order.builder()
                .orderId(orderId)
                .productId(productId)
                .orderState(OrderStateEnum.ORDER_WAIT_PAY)
                .build();
        redisCommonProcessor.set(orderId,order,900);
        return order;
    }

    /**
     * 支付订单
     *
     * @param orderId
     * @return
     */
    @Override
    public Order payOrder(String orderId) throws Exception {

        Order order = (Order)redisCommonProcessor.get(orderId);

        //包装订单状态变更message，并附带订单操作PAY_ORDER
        Message message= MessageBuilder
                            .withPayload(OrderStateChangeActionEnum.PAY_ORDER)
                            .setHeader("order", order)
                            .build();

        //将状态传给spring状态机
        if(changeStateAction(message,order)){
            return order;
        }

        return null;
    }

    /**
     * 订单发货
     *
     * @param orderId
     * @return
     */
    @Override
    public Order sendOrder(String orderId) throws Exception {
        Order order = (Order)redisCommonProcessor.get(orderId);

        //包装订单状态变更message，并附带订单操作SEND_ORDER
        Message message= MessageBuilder
                .withPayload(OrderStateChangeActionEnum.SEND_ORDER)
                .setHeader("order", order)
                .build();

        //将状态传给spring状态机
        if(changeStateAction(message,order)){
            return order;
        }
        return null;
    }

    /**
     * 订单结束
     *
     * @param orderId
     * @return
     */
    @Override
    public Order receiveOrder(String orderId) throws Exception {
        Order order = (Order)redisCommonProcessor.get(orderId);

        //包装订单状态变更message，并附带订单操作RECEIVE_ORDER
        Message message= MessageBuilder
                .withPayload(OrderStateChangeActionEnum.RECEIVE_ORDER)
                .setHeader("order", order)
                .build();

        //将状态传给spring状态机
        if(changeStateAction(message,order)){
            return order;
        }
        return null;
    }

    /**
     * 订单状态发生改变的操作
     * @param message
     * @param order
     * @return
     */
    private boolean changeStateAction(Message<OrderStateChangeActionEnum> message, Order order) throws Exception {
        try {
            //启动状态机
            orderStateMachine.start();

            //从缓存读取状态机信息
            stateMachinePersister.persist(orderStateMachine, order.getOrderId() + "STATE");

            //将Message发给监听器
            boolean res = orderStateMachine.sendEvent(message);

            //将修改后的订单状态的状态机存到redis
            stateMachinePersister.persist(orderStateMachine,order.getOrderId()+ "STATE");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            orderStateMachine.stop();
        }
        return false;
    }
}
