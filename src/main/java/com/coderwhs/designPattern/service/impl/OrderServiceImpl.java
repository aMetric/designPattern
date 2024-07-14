package com.coderwhs.designPattern.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.coderwhs.designPattern.common.ErrorCode;
import com.coderwhs.designPattern.constant.PayConstant;
import com.coderwhs.designPattern.exception.ThrowUtils;
import com.coderwhs.designPattern.model.entity.Order;
import com.coderwhs.designPattern.model.enums.OrderStateChangeActionEnum;
import com.coderwhs.designPattern.model.enums.OrderStateEnum;
import com.coderwhs.designPattern.orderManagement.command.OrderCommandImpl;
import com.coderwhs.designPattern.orderManagement.command.invoker.OrderCommandInvoker;
import com.coderwhs.designPattern.pay.facade.PayFacade;
import com.coderwhs.designPattern.service.OrderService;
import com.coderwhs.designPattern.utils.RedisCommonProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    @Autowired
    private OrderCommandImpl orderCommand;

    @Autowired
    private PayFacade payFacade;

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
     * 支付回调接口
     *
     * @param request
     * @return
     */
    @Override
    public String alipayCallback(HttpServletRequest request) throws Exception {

        // 获取回调信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iterator = requestParams.keySet().iterator();iterator.hasNext();){
            String name = (String)iterator.next();
            String[] valueArr = (String[])requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < valueArr.length; i++) {
                valueStr = (i == valueArr.length -1) ? valueStr + valueArr[i] : valueStr + valueArr[i] + ",";
            }
            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
            params.put(name,valueStr);
        }

        // 验证签名，确保回调接口确实是支付宝平台触发的
        boolean signVerfied = AlipaySignature.rsaCheckV1(params, PayConstant.ALIPAY_PUBLIC_KEY, String.valueOf(StandardCharsets.UTF_8), PayConstant.SIGN_TYPE);

        // 支付失败则抛出异常
        ThrowUtils.throwIf(!signVerfied, ErrorCode.OPERATION_ERROR,"callback verify failed");

        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        String trade_no = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
        float total_amount = Float.parseFloat(new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));

        // 进行相关的业务操作，修改订单状态为待发货状态
        Order order = payOrder(out_trade_no);

        return "支付成功页面跳转，当前订单为："+order;
    }

    //获取支付链接
    @Override
    public String getPayUrl(String orderId,BigDecimal price,Integer payType) throws Exception {
        Order order = (Order)redisCommonProcessor.get(orderId);
        order.setPrice(price);
        return payFacade.pay(order,payType);
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
