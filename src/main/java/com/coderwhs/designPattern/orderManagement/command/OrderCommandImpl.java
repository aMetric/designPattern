package com.coderwhs.designPattern.orderManagement.command;

import com.coderwhs.designPattern.model.entity.Order;
import com.coderwhs.designPattern.orderManagement.command.receiver.OrderCommandReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author whs
 * @Date 2024/7/6 20:54
 * @description: 具体命令角色
 */
@Component
public class OrderCommandImpl implements OrderCommandInterface{

    @Autowired
    private OrderCommandReceiver commandReceiver;

    @Override
    public void execute(Order order) {
        commandReceiver.action(order);
    }
}
