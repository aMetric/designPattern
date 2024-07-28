package com.coderwhs.designPattern.template;

import org.springframework.stereotype.Component;

/**
 * @author whs
 */
@Component
public class CreateOrderLog extends AbstractAuditLogProcessor{

    @Override
    protected OrderAuditLog buildDetails(OrderAuditLog auditLog) {
        return auditLog;
    }
}
