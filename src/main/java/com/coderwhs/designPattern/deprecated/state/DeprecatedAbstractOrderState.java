package com.coderwhs.designPattern.deprecated.state;

import com.coderwhs.designPattern.deprecated.DeprecatedConstants;
import com.coderwhs.designPattern.deprecated.observer.DeprecatedAbstarctObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @Author whs
 * @Date 2024/6/23 11:32
 * @description: 抽象状态类-定义订单状态和方法
 */
public abstract class DeprecatedAbstractOrderState {
    // 待支付
    protected final String ORDER_WAIT_PAY = "ORDER_WAIT_PAY";
    // 待发货
    protected final String ORDER_WAIT_SEND = "ORDER_WAIT_SEND";
    // 待收货
    protected final String ORDER_WAIT_RECEIVE = "ORDER_WAIT_RECEIVE";
    // 订单完成
    protected final String ORDER_FINISH = "ORDER_FINISH";

    /**
     * 订单方法定义-创建订单
     * @param orderId
     * @param productId
     * @return
     */
    protected DeprecatedOrder createOrder(String orderId,String productId){
        throw new UnsupportedOperationException();
    }

    /**
     * 订单方法定义-创建订单
     * @param orderId
     * @return
     */
    protected DeprecatedOrder payOrder(String orderId){
        throw new UnsupportedOperationException();
    }

    /**
     * 订单方法定义-订单支付
     * @param orderId
     * @return
     */
    protected DeprecatedOrder sendOrder(String orderId){
        throw new UnsupportedOperationException();
    }

    /**
     * 订单方法定义-订单发送
     * @param orderId
     * @return
     */
    protected DeprecatedOrder receiveOrder(String orderId){
        throw new UnsupportedOperationException();
    }

    //关联抽象观察者。以List形式进行关联，因为要支持观察者的添加和删除操作
    protected final List<DeprecatedAbstarctObserver> observerList = DeprecatedConstants.OBSERVER_LIST;

    //新增观察者
    public void addObserver(DeprecatedAbstarctObserver observer){
        this.observerList.add(observer);
    }

    //移除观察者
    public void removeObserver(DeprecatedAbstarctObserver observer){
        this.observerList.remove(observer);
    }

    //通知观察者进行相关操作，并调用rderStateHandle方法
    public void notifyObserver(String orderId,String orderState){
        this.observerList.forEach(e->e.orderStateHandle(orderId,orderState));
    }
}
