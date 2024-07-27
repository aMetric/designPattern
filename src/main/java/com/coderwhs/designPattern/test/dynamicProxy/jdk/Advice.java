package com.coderwhs.designPattern.test.dynamicProxy.jdk;

/**
 * @Author whs
 * @Date 2024/7/27 17:13
 * @description: 为代理对象增加额外的前置处理逻辑和后置处理逻辑
 */
public class Advice {
    public void before(){
        System.out.println("前置处理方法");
    }

    public void after(){
        System.out.println("后置处理方法");
    }
}
