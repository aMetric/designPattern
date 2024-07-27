package com.coderwhs.designPattern.ticket.director;

/**
 * @Author whs
 * @Date 2024/7/26 11:16
 * @description: 抽象导演类
 */
public abstract class AbstractDirector {
    /**
     *
     * @param type personal代表个人电子发票，company代表企业电子发票
     * @param product
     * @param content
     * @param title
     * @param bankInfo
     * @param taxId
     * @return
     */
    public abstract Object buildTicket(String type,String product,String content,String title,String bankInfo,String taxId);
}
