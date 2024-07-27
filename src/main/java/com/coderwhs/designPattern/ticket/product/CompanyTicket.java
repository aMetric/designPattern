package com.coderwhs.designPattern.ticket.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whs
 * 企业电子发票
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyTicket implements Cloneable{
    //发票固定不变的信息
    private String finalInfo;
    //发票抬头
    private String title;
    //企业税号
    private String taxId;
    //银行卡信息
    private String bankInfo;
    //商品信息
    private String product;
    //税率、发票代码、校验码、收款方等信息
    private String content;

    @Override
    public CompanyTicket clone() {
        try {
            CompanyTicket clone = (CompanyTicket) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
