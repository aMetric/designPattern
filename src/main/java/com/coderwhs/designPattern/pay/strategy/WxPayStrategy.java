package com.coderwhs.designPattern.pay.strategy;

import com.coderwhs.designPattern.model.entity.Order;

/**
 * @Author whs
 * @Date 2024/7/12 19:43
 * @description: 微信支付
 */
public class WxPayStrategy implements IPayStrategy{
    @Override
    public String pay(Order order) {
        return null;
    }
}
