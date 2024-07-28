package com.coderwhs.designPattern.mediator;

import com.coderwhs.designPattern.mediator.colleage.AbstractCustomer;
import com.coderwhs.designPattern.mediator.colleage.Buyer;
import com.coderwhs.designPattern.mediator.colleage.Payer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author whs
 * @Date 2024/7/28 11:26
 * @description: 具体中介者
 */
@Component
public class Mediator extends AbstractMediator{

    //关联多个对应的同事类,orderId为key
    public static Map<String, Map<String,AbstractCustomer>> customerInstMap = new ConcurrentHashMap<>();

    /**
     * 信息交互方法
     *
     * @param orderId        订单id
     * @param targetCustomer 目标用户
     * @param customer       抽象同事类
     * @param payResult      支付结果
     *  1.将待支付的商品订单给实际支付者
     *  2.实际支付者支付之后转发支付结果给商品购买者
     */
    @Override
    public void msgTransfer(String orderId, String targetCustomer, AbstractCustomer customer, String payResult) {
        if(customer instanceof Buyer) {
            AbstractCustomer buyer = customerInstMap.get(orderId).get("buyer");
            System.out.println("朋友代付："+ buyer.getCustomerName() +"转发 OrderId " + orderId + " 到用户 " + targetCustomer + " 进行支付." );
        } else if(customer instanceof Payer) {
            AbstractCustomer payer = customerInstMap.get(orderId).get("payer");
            System.out.println("代付完成："+ payer.getCustomerName() +"完成 OrderId " + orderId + " 的支付。通知 " + targetCustomer + "，支付结果：" + payResult );
            customerInstMap.remove(orderId);
        }
    }
}
