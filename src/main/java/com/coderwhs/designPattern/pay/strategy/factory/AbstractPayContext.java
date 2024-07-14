package com.coderwhs.designPattern.pay.strategy.factory;

import com.coderwhs.designPattern.model.entity.Order;

/**
 * @Author whs
 * @Date 2024/7/13 22:33
 * @description: 抽象产品类
 */
public abstract class AbstractPayContext {
    public abstract String execute(Order order);
}
