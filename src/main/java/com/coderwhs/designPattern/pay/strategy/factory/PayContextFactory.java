package com.coderwhs.designPattern.pay.strategy.factory;

import com.coderwhs.designPattern.common.ErrorCode;
import com.coderwhs.designPattern.exception.ThrowUtils;
import com.coderwhs.designPattern.model.enums.StrategyEnum;
import com.coderwhs.designPattern.pay.strategy.IPayStrategy;
import com.coderwhs.designPattern.pay.strategy.PayContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author whs
 * @Date 2024/7/13 22:28
 * @description: 具体工厂类-工厂模式
 * PAY_CONTEXT_MAP的作用是避免对象的多次创建，减小高并发下的内存压力，实现对象的重复利用，也是亨元模式的实现思想
 */
@Component
public class PayContextFactory extends AbstractPayContextFactory<PayContext>{

    // 缓存payContext
    private static final Map<String,PayContext> PAY_CONTEXT_MAP = new ConcurrentHashMap<>();

    @Override
    public PayContext getContext(Integer payType) {

        // 根据payType获取枚举类
        StrategyEnum strategyEnum = payType == 1 ? StrategyEnum.alipay :
                                    payType == 2 ? StrategyEnum.wechat :
                                    null;
        ThrowUtils.throwIf(strategyEnum == null, ErrorCode.NOT_FOUND_ERROR,"payType not supported!");

        // 从PAY_CONTEXT_MAP中获取payContext
        PayContext context = PAY_CONTEXT_MAP.get(strategyEnum.getValue());

        // 第一次调用，context为空的情况，根据反射获取
        if (context == null){
            try {
                // 通过反射获取具体策略类
                IPayStrategy iPayStrategy = (IPayStrategy)Class.forName(strategyEnum.getValue()).newInstance();
                // 将具体策略类作为入参，创建PayContext
                PayContext payContext = new PayContext(iPayStrategy);
                // 将PayContext类存储到PAY_CONTEXT_MAP，后续可以直接使用
                PAY_CONTEXT_MAP.put(strategyEnum.getValue(),payContext);
            } catch (Exception e) {
                throw new UnsupportedOperationException("get payStrategy failed!");
            }
        }
        return PAY_CONTEXT_MAP.get(strategyEnum.getValue());
    }
}
