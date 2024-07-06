package com.coderwhs.designPattern.orderManagement.command;

import com.coderwhs.designPattern.model.entity.Order;

/**
 * @Author whs
 * @Date 2024/7/6 20:50
 * @description: 抽象命令角色
 */
public interface OrderCommandInterface {
    //执行命令
    void execute(Order order);
}
