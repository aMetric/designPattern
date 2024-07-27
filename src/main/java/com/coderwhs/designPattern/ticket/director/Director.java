package com.coderwhs.designPattern.ticket.director;

import com.coderwhs.designPattern.ticket.builder.CompanyTicketBuilder;
import com.coderwhs.designPattern.ticket.builder.PersonalTicketBuilder;
import org.springframework.stereotype.Component;

/**
 * @Author whs
 * @Date 2024/7/26 11:18
 * @description: 导演类
 */
@Component
public class Director extends AbstractDirector{
    /**
     * @param type personal代表个人电子发票，company代表企业电子发票
     * @param product
     * @param content
     * @param title
     * @param bankInfo
     * @param taxId
     * @return
     */
    @Override
    public Object buildTicket(String type, String product, String content, String title, String bankInfo, String taxId) {
        if("personal".equals(type)){
            PersonalTicketBuilder personalTicketBuilder = new PersonalTicketBuilder();
            personalTicketBuilder.setCommonInfo(title,product,content);
            return personalTicketBuilder.buildTicket();
        }else if("company".equals(type)){
            CompanyTicketBuilder companyTicketBuilder = new CompanyTicketBuilder();
            companyTicketBuilder.setCommonInfo(title,product,content);
            companyTicketBuilder.setTaxId(taxId);
            companyTicketBuilder.setBankInfo(bankInfo);
        }else{
            throw new UnsupportedOperationException("不支持的发票类型");
        }
        return null;
    }
}
