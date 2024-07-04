package com.coderwhs.designPattern.deprecated.observer;

import com.coderwhs.designPattern.deprecated.DeprecatedConstants;
import com.coderwhs.designPattern.model.enums.OrderStateEnum;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author whs
 * @Date 2024/7/5 7:07
 * @description:
 */
@Component
public class DeprecatedCreateObserver extends DeprecatedAbstarctObserver{

    //程序启动时候将自己添加到OBSERVER_LIST
    @PostConstruct
    public void init(){
        DeprecatedConstants.OBSERVER_LIST.add(this);
    }

    @Override
    public void orderStateHandle(String orderId, String orderState) {

        if(!orderState.equals("ORDER_WAIT_RECEIVE")){
            return;
        }

        //通过命令模式进行后续处理
        System.out.println("监听到：订单创建成功。通过命令模式进行后续处理");
    }
}
