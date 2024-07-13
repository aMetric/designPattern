package com.coderwhs.designPattern.pay.strategy;

import com.alipay.api.AlipayApiException;
import com.coderwhs.designPattern.model.entity.Order;

/**
 * @Author whs
 * @Date 2024/7/12 19:30
 * @description: 抽象策略类
 */
public interface IPayStrategy {
    //公共支付方法
    String pay(Order order) throws AlipayApiException;
}
