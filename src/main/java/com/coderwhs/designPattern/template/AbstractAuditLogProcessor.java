package com.coderwhs.designPattern.template;

import java.util.Date;

/**
 * @Author whs
 * @Date 2024/7/28 15:54
 * @description: 抽象模板类
 */
public abstract class AbstractAuditLogProcessor {
    /**
     * 生成基本固定信息
     * private final意味着方法不可被重写——与模板方法的基本思想需保持一致
     * @param account
     * @param action
     * @param orderId
     * @return
     */
    private final OrderAuditLog basicAuditLog(String account,String action,String orderId){
        OrderAuditLog auditLog = new OrderAuditLog();
        auditLog.setAccount(account);
        auditLog.setAction(action);
        auditLog.setDate(new Date());
        auditLog.setOrderId(orderId);
        return auditLog;
    }

    //定义抽象模板方法，设置订单审计日志的额外信息，供子类进行实现
    protected abstract OrderAuditLog buildDetails(OrderAuditLog auditLog);

    /**
     * 定义订单审计日志的创建步骤-模板方法的核心逻辑
     * @param account
     * @param action
     * @param orderId
     * @return
     */
    public final OrderAuditLog createAuditLog(String account,String action,String orderId){
        //生成审计日志的基本信息
        OrderAuditLog auditLog = basicAuditLog(account, action, orderId);
        //设置审计日志的其他信息
        return buildDetails(auditLog);
    }
}
