package com.coderwhs.designPattern.template;

import org.springframework.stereotype.Component;

/**
 * @author whs
 */
@Component
public class ReceiveOrderLog extends AbstractAuditLogProcessor{
    @Override
    protected OrderAuditLog buildDetails(OrderAuditLog auditLog) {
        //无需进行额外操作，直接返回
        return auditLog;
    }
}
