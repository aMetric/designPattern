package com.coderwhs.designPattern.controller;

import com.coderwhs.designPattern.common.BaseResponse;
import com.coderwhs.designPattern.common.ErrorCode;
import com.coderwhs.designPattern.common.ResultUtils;
import com.coderwhs.designPattern.model.entity.Order;
import com.coderwhs.designPattern.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        Order order = orderService.createOrder(productId);
        if(order != null){
            return ResultUtils.success(order);
        }
        return ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PostMapping("/pay")
    public BaseResponse<Order> payOrder(@RequestParam String orderId) throws Exception {
        Order order = orderService.payOrder(orderId);
        if(order != null){
            return ResultUtils.success(order);
        }
        return ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PostMapping("/send")
    public BaseResponse<Order> sendOrder(@RequestParam String orderId) throws Exception {
        Order order = orderService.sendOrder(orderId);
        if(order != null){
            return ResultUtils.success(order);
        }
        return ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }

    @PostMapping("/receive")
    public BaseResponse<Order> receiveOrder(@RequestParam String orderId) throws Exception {
        Order order = orderService.receiveOrder(orderId);
        if(order != null){
            return ResultUtils.success(order);
        }
        return ResultUtils.error(ErrorCode.OPERATION_ERROR);
    }
}
