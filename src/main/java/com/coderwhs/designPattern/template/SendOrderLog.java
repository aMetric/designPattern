package com.coderwhs.designPattern.template;

import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author whs
 */
@Component
public class SendOrderLog extends AbstractAuditLogProcessor{
    @Override
    protected OrderAuditLog buildDetails(OrderAuditLog auditLog) {
        //增加支付类型和实际支付金额
        HashMap<String, String> extraLog = new HashMap<>();
        extraLog.put("快递公司","X速递");
        extraLog.put("快递编号", "100100100");
        auditLog.setDetails(extraLog);
        return auditLog;
    }
}
