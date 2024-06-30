package com.coderwhs.designPattern.controller;

import com.coderwhs.designPattern.deprecated.service.DeprecatedOrderService;
import com.coderwhs.designPattern.deprecated.state.DeprecatedOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deprecated/order")
public class DeprecatedOrderController {

    @Autowired
    private DeprecatedOrderService deprecatedOrderService;

    @PostMapping("/create")
    public DeprecatedOrder createOrder(@RequestParam String productId) {
        return deprecatedOrderService.createOrder(productId);
    }

    @PostMapping("/pay")
    public DeprecatedOrder payOrder(@RequestParam String orderId){
        return deprecatedOrderService.pay(orderId);
    }

    @PostMapping("/send")
    public DeprecatedOrder send(@RequestParam String orderId) {
        return deprecatedOrderService.send(orderId);
    }

    @PostMapping("/receive")
    public DeprecatedOrder receive(@RequestParam String orderId) {
        return deprecatedOrderService.receive(orderId);
    }
}
