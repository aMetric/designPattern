package com.coderwhs.designPattern.model.entity;

import com.coderwhs.designPattern.model.enums.OrderState;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
    private String orderId;
    private String productId;
    private OrderState orderState;//订单状态
    private Float price;//商品价格
    private String userId;//当前用户唯一Id
}
