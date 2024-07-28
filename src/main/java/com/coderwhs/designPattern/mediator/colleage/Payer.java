package com.coderwhs.designPattern.mediator.colleage;

import com.coderwhs.designPattern.mediator.AbstractMediator;

/**
 * @Author whs
 * @Date 2024/7/28 11:25
 * @description: 实际支付者
 */
public class Payer extends AbstractCustomer{
    public Payer(AbstractMediator abstractMediator, String orderId, String customerName) {
        super(abstractMediator, orderId, customerName);
    }

    @Override
    public void msgTransfer(String orderId, String targetCustomer, String payResult) {
        super.abstractMediator.msgTransfer(orderId, targetCustomer,this,payResult);
    }
}
