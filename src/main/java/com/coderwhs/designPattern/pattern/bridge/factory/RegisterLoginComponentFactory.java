package com.coderwhs.designPattern.pattern.bridge.factory;

import com.coderwhs.designPattern.pattern.bridge.AbstractRegisterLoginComponent;
import com.coderwhs.designPattern.pattern.bridge.IRegisterLoginFunc;
import com.coderwhs.designPattern.pattern.bridge.RegisterLoginComponent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author wuhs
 * @Date 2024/4/21 16:46
 * @Description 引入工厂类进行RegisterLoginComponent对象的生成和缓存
 */
public class RegisterLoginComponentFactory {
    //缓存AbstractRegisterLoginComponent（左路），根据不同登入方式进行缓存
    public static Map<String, AbstractRegisterLoginComponent> COMPONENT_MAP = new ConcurrentHashMap<>();

    //缓存不同实现类型的实现类（右路）
    public static Map<String, IRegisterLoginFunc> FUNC_MAP = new ConcurrentHashMap<>();

    //根据不同登入类型，获取AbstractRegisterLoginComponent
    public static AbstractRegisterLoginComponent getComponent(String type){
        System.out.println("==========================type = " + type);
        AbstractRegisterLoginComponent component = COMPONENT_MAP.get(type);

        if (component == null){
            //并发情况下，汲取双重检查锁的机制设计，若componentMap里面没有，则进行创建
            synchronized (COMPONENT_MAP){
                component = COMPONENT_MAP.get(type);

                //根据不同类型的实现类（右路），创建AbstractRegisterLoginComponent对象
                if (component == null){
                    component = new RegisterLoginComponent(FUNC_MAP.get(type));
                    COMPONENT_MAP.put(type,component);
                }
            }
        }
        return component;
    }
}
