package com.coderwhs.designPattern.pay.strategy;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.coderwhs.designPattern.constant.PayConstant;
import com.coderwhs.designPattern.model.entity.Order;

/**
 * @Author whs
 * @Date 2024/7/12 19:42
 * @description: 支付宝支付
 */
public class AliPayStrategy implements IPayStrategy{
    @Override
    public String pay(Order order){

        //创建AliPayClient
        AlipayClient alipayClient = new DefaultAlipayClient(PayConstant.ALIPAY_GATEWAY_URL, PayConstant.APP_ID,
                PayConstant.APP_PRIVATE_KEY, "JSON","UTF-8",PayConstant.ALIPAY_PUBLIC_KEY, PayConstant.SIGN_TYPE);

        // 设置请求参数
        AlipayTradePagePayRequest payRequest = new AlipayTradePagePayRequest();
        payRequest.setReturnUrl(PayConstant.CALL_BACK_URL);
        payRequest.setBizContent("{\"out_trade_no\":\"" + order.getOrderId() + "\","
                + "\"total_amount\":\"" + order.getPrice() + "\","
                + "\"subject\":\"" + "吴海胜" + "\","
                + "\"body\":\"" + "商品描述" + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String res = null;
        try {
            res = alipayClient.pageExecute(payRequest, "GET").getBody();
        } catch (AlipayApiException e) {
            throw new RuntimeException("Alipay failed"+e);
        }
        return res;
    }
}
