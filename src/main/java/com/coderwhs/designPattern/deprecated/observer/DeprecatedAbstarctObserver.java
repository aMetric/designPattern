package com.coderwhs.designPattern.deprecated.observer;

/**
 * @Author whs
 * @Date 2024/7/5 7:03
 * @description: 抽象观察者
 */
public abstract class DeprecatedAbstarctObserver {
    //订单状态变更时候，调用此方法
    public abstract void orderStateHandle(String orderId,String orderState);
}
