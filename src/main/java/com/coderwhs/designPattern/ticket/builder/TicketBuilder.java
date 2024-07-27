package com.coderwhs.designPattern.ticket.builder;

/**
 * @Author whs
 * @Date 2024/7/26 10:55
 * @description: 抽象建造者
 */
public abstract class TicketBuilder <T>{
    //设置通用发票信息
    public abstract void setCommonInfo(String title,String product,String content);

    //设置企业税号
    public void setTaxId(String taxId){
        throw new UnsupportedOperationException();
    }

    //设置企业银行卡信息
    public void setBankInfo(String backInfo){
        throw new UnsupportedOperationException();
    }

    //抽象建造方法
    public abstract T buildTicket();
}
