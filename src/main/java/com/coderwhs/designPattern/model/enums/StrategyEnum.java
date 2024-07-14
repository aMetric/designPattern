package com.coderwhs.designPattern.model.enums;

/**
 * @Author whs
 * @Date 2024/7/14 10:20
 * @description: 策略枚举类
 */
public enum StrategyEnum {
    //定义支付宝策略类
    alipay("com.coderwhs.designPattern.pay.strategy.AliPayStrategy"),
    // 定义微信支付策略类
    wechat("com.coderwhs.designPattern.pay.strategy.WxPayStrategy");

    String value = "";

    StrategyEnum(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
