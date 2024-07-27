package com.coderwhs.designPattern.test.dynamicProxy.cglib;

import com.coderwhs.designPattern.test.dynamicProxy.jdk.Advice;
import com.coderwhs.designPattern.test.dynamicProxy.jdk.Subject;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author whs
 * @Date 2024/7/27 18:11
 * @description:
 */
public class CglibProxyTest {
    public static void main(String[] args) {
        //被代理对象
        Subject subject = new Subject();
        //增强对象
        Advice advice = new Advice();
        //创建增强器
        Enhancer enhancer = new Enhancer();
        //设置增强目标类
        enhancer.setSuperclass(Subject.class);
        //设置回调
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                advice.before();
                Object invoke = method.invoke(subject, args);
                advice.after();
                return invoke;
            }
        });
        //创建代理，调用核心方法
        Subject targetProxy = (Subject)enhancer.create();
        //执行核心方法
        targetProxy.coreMethod();
    }
}
