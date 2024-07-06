package com.coderwhs.designPattern.orderManagement.lister;

import com.coderwhs.designPattern.common.ErrorCode;
import com.coderwhs.designPattern.exception.ThrowUtils;
import com.coderwhs.designPattern.model.entity.Order;
import com.coderwhs.designPattern.model.enums.OrderStateChangeActionEnum;
import com.coderwhs.designPattern.model.enums.OrderStateEnum;
import com.coderwhs.designPattern.orderManagement.command.OrderCommandImpl;
import com.coderwhs.designPattern.orderManagement.command.invoker.OrderCommandInvoker;
import com.coderwhs.designPattern.utils.RedisCommonProcessor;
import com.coderwhs.designPattern.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Component;

/**
 * @Author whs
 * @Date 2024/7/6 16:24
 * @description: 监听状态机
 */
@Component
@WithStateMachine(name = "orderStateMachine")
public class OrderStateLister {

    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    //持久化状态机
    @Autowired
    private StateMachinePersister<OrderStateEnum, OrderStateChangeActionEnum,String> stateMachinePersister;

    @Autowired
    private OrderCommandImpl orderCommand;

    /**
     * 支付状态->发货状态
     * @return
     */
    @OnTransition(source = "ORDER_WAIT_PAY",target = "ORDER_WAIT_SEND")
    public boolean payToSend(Message<OrderStateChangeActionEnum> message){

        //从redis获取订单，并判断状态是否为待支付
        Order order = (Order)message.getHeaders().get(RedisKeyUtils.ORDER_KEY);
        ThrowUtils.throwIf(!order.getOrderState().equals(OrderStateEnum.ORDER_WAIT_PAY), ErrorCode.PARAMS_ERROR,"订单状态错误");

        //支付成功后修改订单状态为待发货，并更新redis
        order.setOrderState(OrderStateEnum.ORDER_WAIT_SEND);
        redisCommonProcessor.set(order.getOrderId(), order);

        //命令模式进行处理
        new OrderCommandInvoker().invoke(orderCommand,order);

        return true;
    }

    /**
     * 发货状态->收货状态
     * @return
     */
    @OnTransition(source = "ORDER_WAIT_SEND", target = "ORDER_WAIT_RECEIVE")
    public boolean sendToReceive(Message<OrderStateChangeActionEnum> message){
        Order order = (Order) message.getHeaders().get(RedisKeyUtils.ORDER_KEY);
        ThrowUtils.throwIf(order.getOrderState() != OrderStateEnum.ORDER_WAIT_SEND, ErrorCode.PARAMS_ERROR,"订单状态错误");

        order.setOrderState(OrderStateEnum.ORDER_WAIT_RECEIVE);
        redisCommonProcessor.set(order.getOrderId(), order);

        //命令模式进行相关处理
        new OrderCommandInvoker().invoke(orderCommand,order);

        return true;
    }


    /**
     * 收货状态->结束状态
     * @return
     */
    @OnTransition(source = "ORDER_WAIT_RECEIVE", target = "ORDER_FINISH")
    public boolean receiveToFinish(Message<OrderStateChangeActionEnum> message){
        Order order = (Order) message.getHeaders().get(RedisKeyUtils.ORDER_KEY);
        ThrowUtils.throwIf(order.getOrderState() != OrderStateEnum.ORDER_WAIT_RECEIVE, ErrorCode.PARAMS_ERROR,"订单状态错误");

        order.setOrderState(OrderStateEnum.ORDER_FINISH);

        //删除订单信息和状态机信息
        redisCommonProcessor.remove(order.getOrderId());
        redisCommonProcessor.remove(order.getOrderId() + "STATE");

        //命令模式进行相关处理
        new OrderCommandInvoker().invoke(orderCommand,order);

        return true;
    }
}
