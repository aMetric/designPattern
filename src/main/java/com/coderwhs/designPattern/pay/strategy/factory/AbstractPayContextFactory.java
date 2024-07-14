package com.coderwhs.designPattern.pay.strategy.factory;

/**
 * @Author whs
 * @Date 2024/7/13 22:31
 * @description: 具体工厂类
 */
public abstract class AbstractPayContextFactory<T> {
    public abstract T getContext(Integer payType);
}
