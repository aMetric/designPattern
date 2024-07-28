package com.coderwhs.designPattern.service.impl;

import com.coderwhs.designPattern.mediator.Mediator;
import com.coderwhs.designPattern.mediator.colleage.AbstractCustomer;
import com.coderwhs.designPattern.mediator.colleage.Buyer;
import com.coderwhs.designPattern.mediator.colleage.Payer;
import com.coderwhs.designPattern.model.entity.Order;
import com.coderwhs.designPattern.model.enums.OrderStateChangeActionEnum;
import com.coderwhs.designPattern.model.enums.OrderStateEnum;
import com.coderwhs.designPattern.orderManagement.command.OrderCommandImpl;
import com.coderwhs.designPattern.orderManagement.command.invoker.OrderCommandInvoker;
import com.coderwhs.designPattern.pay.facade.PayFacade;
import com.coderwhs.designPattern.service.inter.OrderServiceInterface;
import com.coderwhs.designPattern.template.*;
import com.coderwhs.designPattern.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @Author whs
 * @Date 2024/7/6 17:07
 * @description: 订单状态处理
 */
@Service
@Qualifier("OrderServiceImpl")
public class OrderServiceImpl implements OrderServiceInterface {

    @Autowired
    private StateMachine<OrderStateEnum, OrderStateChangeActionEnum> orderStateMachine;

    //持久化状态机
    @Autowired
    private StateMachinePersister<OrderStateEnum, OrderStateChangeActionEnum,String> stateMachinePersister;

    @Autowired
    private RedisCommonProcessor redisCommonProcessor;

    @Autowired
    private OrderCommandImpl orderCommand;

    @Autowired
    private PayFacade payFacade;

    @Autowired
    private Mediator mediator;

    @Autowired
    private CreateOrderLog createOrderLog;

    @Autowired
    private PayOrderLog payOrderLog;

    @Autowired
    private ReceiveOrderLog receiveOrderLog;

    @Autowired
    private SendOrderLog sendOrderLog;

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

        //订单创建初始过程没有被状态机管理，没有被监听
        new OrderCommandInvoker().invoke(orderCommand, order);

        OrderAuditLog auditLog = createOrderLog.createAuditLog("0", OrderStateChangeActionEnum.CREATE_ORDER.name(), orderId);
        //todo 保存日志
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
        OrderAuditLog auditLog = payOrderLog.createAuditLog("0", OrderStateChangeActionEnum.PAY_ORDER.name(), orderId);
        //todo 保存日志
        return order;
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
        OrderAuditLog auditLog = sendOrderLog.createAuditLog("0", OrderStateChangeActionEnum.SEND_ORDER.name(), orderId);
        //todo 保存日志
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
        OrderAuditLog auditLog = receiveOrderLog.createAuditLog("0", OrderStateChangeActionEnum.RECEIVE_ORDER.name(), orderId);
        //todo 保存日志
        return null;
    }

    //获取支付链接
    @Override
    public String getPayUrl(String orderId,BigDecimal price,Integer payType) throws Exception {
        Order order = (Order)redisCommonProcessor.get(orderId);
        order.setPrice(price);
        return payFacade.pay(order,payType);
    }

    /**
     * 朋友代付
     * @param customerName
     * @param orderId
     * @param targetCustomer
     * @param payResult
     * @param role
     * @return
     */
    @Override
    public void friendPay(String customerName, String orderId, String targetCustomer, String payResult, String role) {
        Buyer buyer = new Buyer(mediator, orderId, customerName);
        Payer payer = new Payer(mediator, orderId, customerName);
        HashMap<String, AbstractCustomer> map = new HashMap<>();
        map.put("buyer",buyer);
        map.put("payer",payer);
        //将同事类配置到
        Mediator.customerInstMap.put(orderId,map);
        if("B".equals(role)){
            buyer.msgTransfer(orderId,targetCustomer,payResult);
        }else if("P".equals(role)){
            payer.msgTransfer(orderId,targetCustomer,payResult);
        }
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
            stateMachinePersister.restore(orderStateMachine, order.getOrderId() + "STATE");

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
