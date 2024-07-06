package com.coderwhs.designPattern.orderManagement.command.receiver;

import com.coderwhs.designPattern.model.entity.Order;
import org.springframework.stereotype.Component;

/**
 * @Author whs
 * @Date 2024/7/6 20:47
 * @description: 接受者角色
 */
@Component
public class OrderCommandReceiver {
    //接收命令后执行
    public void action(Order order){
        switch (order.getOrderState()){
            case ORDER_WAIT_PAY:
                System.out.println("创建订单：order = " + order);
                System.out.println("存入DB");
                return;
            case ORDER_WAIT_SEND:
                System.out.println("支付订单：order = " + order);
                System.out.println("存入DB");
                System.out.println("通过quene通知财务部门");
                System.out.println("通过quene通知物流部门");
                return;
            case ORDER_WAIT_RECEIVE:
                System.out.println("订单发货：order = " + order);
                System.out.println("存入DB");
                return;
            case ORDER_FINISH:
                System.out.println("接收订单：order = " + order);
                System.out.println("存入DB");
                return;
            default:
                throw new UnsupportedOperationException("订单状态异常");
        }
    }
}
