package com.coderwhs.designPattern.pay.strategy;

import com.alipay.api.AlipayApiException;
import com.coderwhs.designPattern.model.entity.Order;

/**
 * @Author whs
 * @Date 2024/7/12 21:07
 * @description: 策略环境类
 */
public class PayContext {
    //关联抽象策略类
    private IPayStrategy iPayStrategy;

    //设计具体策略
    public PayContext(IPayStrategy iPayStrategy){
        this.iPayStrategy = iPayStrategy;
    }

    //执行策略
    public String execute(Order order) throws Exception {
        return this.iPayStrategy.pay(order);
    }
}
