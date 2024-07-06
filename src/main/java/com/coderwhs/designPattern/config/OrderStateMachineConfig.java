package com.coderwhs.designPattern.config;

import com.coderwhs.designPattern.model.enums.OrderStateChangeActionEnum;
import com.coderwhs.designPattern.model.enums.OrderStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.RepositoryStateMachinePersist;
import org.springframework.statemachine.redis.RedisStateMachineContextRepository;
import org.springframework.statemachine.redis.RedisStateMachinePersister;

import java.util.EnumSet;

/**
 * @Author whs
 * @Date 2024/7/6 15:53
 * @description: spring状态机配置类
 */
@Configuration
@EnableStateMachine(name = "orderStateMachine")
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderStateEnum, OrderStateChangeActionEnum> {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 为新创建的订单设置初始状态
     */
    @Override
    public void configure(StateMachineStateConfigurer<OrderStateEnum, OrderStateChangeActionEnum> states) throws Exception {

        states.withStates()
                //设置订单创建成功后的初始化状态为待支付ORDER_WAIT_PAY
                .initial(OrderStateEnum.ORDER_WAIT_PAY)
                // 将订单状态类的所有状态加载到状态机
                .states(EnumSet.allOf(OrderStateEnum.class));
    }

    /**
     * 配置订单的转化流程
     * @param transitions the {@link StateMachineTransitionConfigurer}
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStateEnum, OrderStateChangeActionEnum> transitions) throws Exception {
        transitions
                //支付状态->发货状态，需要支付操作
                .withExternal()
                .source(OrderStateEnum.ORDER_WAIT_PAY)
                .target(OrderStateEnum.ORDER_WAIT_SEND)
                .event(OrderStateChangeActionEnum.PAY_ORDER)
                .and()
                //发货状态->收货状态，需要发货操作
                .withExternal()
                .source(OrderStateEnum.ORDER_WAIT_SEND)
                .target(OrderStateEnum.ORDER_WAIT_RECEIVE)
                .event(OrderStateChangeActionEnum.SEND_ORDER)
                .and()
                //收货状态->结束状态，需要收货操作
                .withExternal()
                .source(OrderStateEnum.ORDER_WAIT_RECEIVE)
                .target(OrderStateEnum.ORDER_FINISH)
                .event(OrderStateChangeActionEnum.RECEIVE_ORDER);
    }

    @Bean(name = "stateMachineRedisPersister")
    public RedisStateMachinePersister<OrderStateEnum, OrderStateChangeActionEnum> getRedisPersister(){

        RedisStateMachineContextRepository<OrderStateEnum, OrderStateChangeActionEnum> repository
                = new RedisStateMachineContextRepository<>(redisConnectionFactory);

        RepositoryStateMachinePersist p = new RepositoryStateMachinePersist<>(repository);
        return new RedisStateMachinePersister<>(p);
    }

}
