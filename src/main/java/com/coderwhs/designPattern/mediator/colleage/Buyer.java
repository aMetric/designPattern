package com.coderwhs.designPattern.mediator.colleage;

import com.coderwhs.designPattern.mediator.AbstractMediator;

/**
 * @Author whs
 * @Date 2024/7/28 11:16
 * @description: 购买者
 */
public class Buyer extends AbstractCustomer{
    public Buyer(AbstractMediator abstractMediator, String orderId, String customerName) {
        super(abstractMediator, orderId, customerName);
    }

    @Override
    public void msgTransfer(String orderId, String targetCustomer, String payResult) {
        super.abstractMediator.msgTransfer(orderId, targetCustomer,this,payResult);
    }
}
