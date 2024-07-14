package com.coderwhs.designPattern.model.enums;

/**
 * @Author whs
 * @Date 2024/7/14 20:24
 * @description: 具体责任类枚举
 */
public enum DutyChainHandlerEnum {
    //业务投放目的城市
    city("com.coderwhs.designPattern.dutyChain.CityHandler"),
    //业务投放性别群体
    sex("com.coderwhs.designPattern.dutyChain.SexHandler"),
    //业务投放相关产品
    product("com.coderwhs.designPattern.dutyChain.ProductHandler");
    String value;

    DutyChainHandlerEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
