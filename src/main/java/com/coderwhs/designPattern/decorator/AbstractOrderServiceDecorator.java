package com.coderwhs.designPattern.decorator;

import com.coderwhs.designPattern.model.entity.Order;
import com.coderwhs.designPattern.service.inter.OrderServiceInterface;

import java.math.BigDecimal;

/**
 * @Author whs
 * @Date 2024/7/17 20:11
 * @description: 抽象装饰器
 */
public abstract class AbstractOrderServiceDecorator implements OrderServiceInterface {
    private OrderServiceInterface orderServiceInterface;

    public void setOrderServiceInterface(OrderServiceInterface orderServiceInterface) {
        this.orderServiceInterface = orderServiceInterface;
    }

    //覆写 createOrder 方法,但不改变方法逻辑，直接调用orderServiceInterface的 createOrder 方法
    @Override
    public Order createOrder(String productId) {
        return this.orderServiceInterface.createOrder(productId);
    }

    //覆写 send 方法,但不改变方法逻辑，直接调用orderServiceInterface的 send 方法
    @Override
    public Order sendOrder(String orderId) throws Exception {
        return this.orderServiceInterface.sendOrder(orderId);
    }

    //覆写 receive 方法,但不改变方法逻辑，直接调用orderServiceInterface的 receive 方法
    @Override
    public Order receiveOrder(String orderId) throws Exception {
        return this.orderServiceInterface.receiveOrder(orderId);
    }

    //覆写 getPayUrl 方法,但不改变方法逻辑，直接调用orderServiceInterface的 getPayUrl 方法
    @Override
    public String getPayUrl(String orderId, BigDecimal price, Integer payType) throws Exception {
        return this.orderServiceInterface.getPayUrl(orderId,price,payType);
    }

    //覆写pay方法,但不改变方法逻辑，直接调用orderServiceInterface的pay方法
    @Override
    public Order payOrder(String orderId) throws Exception {
        return this.orderServiceInterface.payOrder(orderId);
    }

    /**
     * 定义新的方法，根据userId和productId更新用户积分、发放红包
     * @param productId
     * @param serviceLevel 服务级别（正常服务、延迟服务、暂停服务）
     * @param price 商品价格
     */
    protected abstract void updateScoreAndSendRedPaper(String productId, int serviceLevel, float price);

}
