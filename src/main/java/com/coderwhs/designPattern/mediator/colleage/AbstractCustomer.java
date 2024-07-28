package com.coderwhs.designPattern.mediator.colleage;

import com.coderwhs.designPattern.mediator.AbstractMediator;
import lombok.Data;

/**
 * @Author whs
 * @Date 2024/7/28 11:01
 * @description: 抽象同事类
 */
@Data
public abstract class AbstractCustomer {
    //关联中介者对象
    public AbstractMediator abstractMediator;

    public String orderId;

    public String customerName;

    public AbstractCustomer(AbstractMediator abstractMediator, String orderId, String customerName) {
        this.abstractMediator = abstractMediator;
        this.orderId = orderId;
        this.customerName = customerName;
    }

    public abstract void msgTransfer(String orderId, String targetCustomer, String payResult);
}
