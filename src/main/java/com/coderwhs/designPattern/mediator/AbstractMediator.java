package com.coderwhs.designPattern.mediator;

import com.coderwhs.designPattern.mediator.colleage.AbstractCustomer;

/**
 * @Author whs
 * @Date 2024/7/28 10:57
 * @description:  抽象中介者
 */
public abstract class AbstractMediator {
    /**
     * 信息交互方法
     * @param orderId 订单id
     * @param targetCustomer 目标用户
     * @param customer 抽象同事类
     * @param payResult 支付结果
     */
    public abstract void msgTransfer(String orderId, String targetCustomer, AbstractCustomer customer, String payResult);
}
