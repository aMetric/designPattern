package com.coderwhs.designPattern.decorator;

import com.coderwhs.designPattern.model.entity.Order;
import com.coderwhs.designPattern.model.entity.Products;
import com.coderwhs.designPattern.repo.ProductsRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author whs
 * @Date 2024/7/17 20:32
 * @description: 具体装饰角色
 */
@Service
@Qualifier("OrderServiceDecorator")
public class OrderServiceDecorator extends AbstractOrderServiceDecorator{

    @Value("${delay.service.time}")
    private String delayServiceTime;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 定义新的方法，根据userId和productId更新用户积分、发放红包
     *
     * @param productId
     * @param serviceLevel 服务级别（0-正常服务、1-延迟服务、2-暂停服务）
     * @param price        商品价格
     */
    @Override
    protected void updateScoreAndSendRedPaper(String productId, int serviceLevel, float price) {
        switch (serviceLevel){
            case 0:
                //根据价格的百分之一更新积分
                int score = Math.round(price) / 100;
                System.out.println("正常处理，为用户更新积分，score = " + score);
                //根据属性发放红包
                Products product = productsRepository.findByProductId(productId);
                if (product != null && product.getSendRedBag() == 1){
                    System.out.println("正常处理，为用户发放红包，product = " + product);
                }
                break;
            case 1:
                MessageProperties properties = new MessageProperties();
                //设置消息过期时间
                properties.setExpiration(delayServiceTime);
                Message msg = new Message(productId.getBytes(),properties);
                //向正常队列发送消息
                rabbitTemplate.send("normalExchange","myRKey",msg);
                System.out.println("延迟处理，时间 = " + delayServiceTime);
                break;
            case 2:
                System.out.println(" 暂停服务！ ");
                break;
            default:
                throw new UnsupportedOperationException("不支持的服务级别！");
        }
    }

    //将pay方法与updateScoreAndSendRedPaper方法进行逻辑结合
    public Order decoratorPay(String orderId, int serviceLevel, float price) throws Exception {
        //调用原有 OrderService 的逻辑
        Order order = super.payOrder(orderId);
        //新的逻辑，更新积分、发放用户红包
        try {
            this.updateScoreAndSendRedPaper(order.getProductId(), serviceLevel, price);
        } catch (Exception e) {
            //重试机制。此处积分更新不能影响 支付主流程
        }
        return order;
    }
}
