package com.coderwhs.designPattern.ticket.builder;

import com.coderwhs.designPattern.ticket.product.CompanyTicket;

/**
 * @Author whs
 * @Date 2024/7/26 11:03
 * @description:
 */
public class CompanyTicketBuilder extends TicketBuilder<CompanyTicket> {

    CompanyTicket companyTicket = new CompanyTicket();
    @Override
    public void setCommonInfo(String title, String product, String content) {
        companyTicket.setTitle(title);
        companyTicket.setProduct(product);
        companyTicket.setContent(content);
    }

    @Override
    public void setBankInfo(String backInfo) {
        companyTicket.setBankInfo(backInfo);
    }

    @Override
    public void setTaxId(String taxId) {
        companyTicket.setTaxId(taxId);
    }

    @Override
    public CompanyTicket buildTicket() {
        return companyTicket;
    }
}
