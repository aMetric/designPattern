package com.coderwhs.designPattern.pay.facade;

import com.alipay.api.AlipayApiException;
import com.coderwhs.designPattern.model.entity.Order;
import com.coderwhs.designPattern.pay.strategy.PayContext;
import com.coderwhs.designPattern.pay.strategy.factory.PayContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author whs
 * @Date 2024/7/13 16:28
 * @description: 门面角色
 */
@Component
public class PayFacade {

    @Autowired
    private PayContextFactory payContextFactory;

    /**
     * 获取支付链接
     * @param order
     * @param payType
     * @return
     * @throws Exception
     */
    public String pay(Order order,Integer payType) throws Exception {
        PayContext context = payContextFactory.getContext(payType);
        return context.execute(order);
    }
}
