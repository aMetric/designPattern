package com.coderwhs.designPattern.orderManagement.command.invoker;

import com.coderwhs.designPattern.model.entity.Order;
import com.coderwhs.designPattern.orderManagement.command.OrderCommandInterface;

/**
 * @Author whs
 * @Date 2024/7/6 20:57
 * @description: 命令调用者
 */
public class OrderCommandInvoker {
    public void invoke(OrderCommandInterface commandInterface, Order order){
        commandInterface.execute(order);
    }
}
