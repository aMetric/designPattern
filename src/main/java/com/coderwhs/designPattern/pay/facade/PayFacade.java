package com.coderwhs.designPattern.pay.facade;

import com.alipay.api.AlipayApiException;
import com.coderwhs.designPattern.model.entity.Order;
import com.coderwhs.designPattern.pay.strategy.AliPayStrategy;
import com.coderwhs.designPattern.pay.strategy.PayContext;
import com.coderwhs.designPattern.pay.strategy.WxPayStrategy;
import org.springframework.stereotype.Component;

/**
 * @Author whs
 * @Date 2024/7/13 16:28
 * @description: 门面角色
 */
@Component
public class PayFacade {
    public String pay(Order order,Integer payType) throws AlipayApiException {
        switch (payType){
            case 1:
                AliPayStrategy aliPayStrategy = new AliPayStrategy();
                PayContext alipayContext = new PayContext(aliPayStrategy);
                return alipayContext.execute(order);
            case 2:
                WxPayStrategy wxPayStrategy = new WxPayStrategy();
                PayContext wxpayContext = new PayContext(wxPayStrategy);
                return wxpayContext.execute(order);
            default:
                throw new UnsupportedOperationException("payType not supportd!");
        }
    }
}
