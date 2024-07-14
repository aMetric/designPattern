package com.coderwhs.designPattern.dutyChain;

import com.coderwhs.designPattern.model.entity.BusinessLaunch;

import java.util.List;

/**
 * @Author whs
 * @Date 2024/7/14 19:10
 * @description: 抽象责任类
 */
public abstract class AbstractBusinessHandler {

    // 定义下一个责任类，相当于下一个责任类的指针
    public AbstractBusinessHandler nextHandler;

    // 是否有下一个责任类
    public boolean hasNextHandler(){
        return this.nextHandler != null;
    }

    // 定义抽象责任类方法
    public abstract List<BusinessLaunch> processHandler(List<BusinessLaunch> launchList,String targetCity,String targetSex,String targetProduct);
}
