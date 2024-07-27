package com.coderwhs.designPattern.test.dynamicProxy.jdk;

/**
 * @Author whs
 * @Date 2024/7/27 17:12
 * @description: 具体被代理的对象
 */
public class Subject implements SubjectInterface{
    @Override
    public void coreMethod() {
        System.out.println(" 进行业务处理 ");
    }
}
