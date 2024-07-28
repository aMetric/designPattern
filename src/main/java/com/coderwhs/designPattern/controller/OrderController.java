package com.coderwhs.designPattern.controller;

import com.alipay.api.internal.util.AlipaySignature;
import com.coderwhs.designPattern.common.BaseResponse;
import com.coderwhs.designPattern.common.ErrorCode;
import com.coderwhs.designPattern.common.ResultUtils;
import com.coderwhs.designPattern.constant.PayConstant;
import com.coderwhs.designPattern.decorator.OrderServiceDecorator;
import com.coderwhs.designPattern.exception.ThrowUtils;
import com.coderwhs.designPattern.model.entity.Order;
import com.coderwhs.designPattern.service.inter.OrderServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author whs
 * @Date 2024/7/6 17:02
 * @description: 状态机实现流程转化
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Qualifier("OrderServiceImpl")
    @Autowired
    private OrderServiceInterface orderService;

    @Qualifier("OrderServiceDecorator")
    @Autowired
    private OrderServiceDecorator orderServiceDecorator;

    @Value("${service.level}")
    private Integer serviceLevel;

    @PostMapping("/create")
    public BaseResponse<Order> createOrder(@RequestParam String productId){
        return ResultUtils.success(orderService.createOrder(productId));
    }

    @PostMapping("/pay")
    public BaseResponse<String> payOrder(@RequestParam String orderId,
                                        @RequestParam BigDecimal price,
                                        @RequestParam Integer payType) throws Exception {
        return ResultUtils.success(orderService.getPayUrl(orderId,price,payType));
    }

    @PostMapping("/send")
    public BaseResponse<Order> sendOrder(@RequestParam String orderId) throws Exception {
        return ResultUtils.success(orderService.sendOrder(orderId));

    }

    @PostMapping("/receive")
    public BaseResponse<Order> receiveOrder(@RequestParam String orderId) throws Exception {
        return ResultUtils.success(orderService.receiveOrder(orderId));
    }

    /**
     * 支付回调接口
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/alipayCallback")
    public BaseResponse<String> alipayCallback(HttpServletRequest request) throws Exception {
        // 获取回调信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iterator = requestParams.keySet().iterator(); iterator.hasNext();){
            String name = (String)iterator.next();
            String[] valueArr = (String[])requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < valueArr.length; i++) {
                valueStr = (i == valueArr.length -1) ? valueStr + valueArr[i] : valueStr + valueArr[i] + ",";
            }
            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
            params.put(name,valueStr);
        }

        // 验证签名，确保回调接口确实是支付宝平台触发的
        boolean signVerfied = AlipaySignature.rsaCheckV1(params, PayConstant.ALIPAY_PUBLIC_KEY, String.valueOf(StandardCharsets.UTF_8), PayConstant.SIGN_TYPE);

        // 支付失败则抛出异常
        ThrowUtils.throwIf(!signVerfied, ErrorCode.OPERATION_ERROR,"callback verify failed");

        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        String trade_no = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1),StandardCharsets.UTF_8);
        float total_amount = Float.parseFloat(new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));

        // 进行相关的业务操作，修改订单状态为待发货状态
        orderServiceDecorator.setOrderServiceInterface(orderService);
        Order order = orderServiceDecorator.decoratorPay(out_trade_no, serviceLevel, total_amount);
        return ResultUtils.success("支付成功页面跳转，当前订单为："+order);
    }

    /**
     * 朋友代付
     * @param customerName
     * @param orderId
     * @param targetCustomer
     * @param payResult
     * @param role B-发起朋友代付请求；P-朋友完成支付
     * @return
     */
    @PostMapping("/friendPay")
    public void friendPay(String customerName, String orderId, String targetCustomer, String payResult, String role) {
        orderService.friendPay(customerName, orderId, targetCustomer, payResult, role);
    }
}
