package com.coderwhs.designPattern.ticket.builder;

import com.coderwhs.designPattern.ticket.product.PersonalTicket;

/**
 * @Author whs
 * @Date 2024/7/26 11:01
 * @description: 个人发票
 */
public class PersonalTicketBuilder extends TicketBuilder<PersonalTicket> {

    PersonalTicket personalTicket = new PersonalTicket();

    @Override
    public void setCommonInfo(String title, String product, String content) {
        personalTicket.setTitle(title);
        personalTicket.setContent(content);
        personalTicket.setProduct(product);
    }

    @Override
    public PersonalTicket buildTicket() {
        return personalTicket;
    }
}
