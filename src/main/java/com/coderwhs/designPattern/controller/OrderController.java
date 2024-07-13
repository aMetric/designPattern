package com.coderwhs.designPattern.controller;

import com.coderwhs.designPattern.common.BaseResponse;
import com.coderwhs.designPattern.common.ErrorCode;
import com.coderwhs.designPattern.common.ResultUtils;
import com.coderwhs.designPattern.model.entity.Order;
import com.coderwhs.designPattern.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author whs
 * @Date 2024/7/6 17:02
 * @description: 状态机实现流程转化
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public BaseResponse<Order> createOrder(@RequestParam String productId){
        return ResultUtils.success(orderService.createOrder(productId));
    }

    @PostMapping("/pay")
    public BaseResponse<Order> payOrder(@RequestParam String orderId) throws Exception {
        return ResultUtils.success(orderService.payOrder(orderId));
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
    @PostMapping("/alipayCallback")
    public BaseResponse<String> alipayCallback(HttpServletRequest request) throws Exception {
        return ResultUtils.success(orderService.alipayCallback(request));
    }
}
