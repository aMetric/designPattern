package com.coderwhs.designPattern.test.dynamicProxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author whs
 * @Date 2024/7/27 17:14
 * @description:
 */
public class JdkProxyTest {
    public static void main(String[] args) {
        //被代理对象
        Subject subject = new Subject();
        //增强对象
        Advice advice = new Advice();

        SubjectInterface proxy = (SubjectInterface) Proxy.newProxyInstance(subject.getClass().getClassLoader(), subject.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                advice.before();
                Object invoke = method.invoke(subject, args);
                advice.after();
                return invoke;
            }
        });
        //执行核心方法
        proxy.coreMethod();
    }
}
