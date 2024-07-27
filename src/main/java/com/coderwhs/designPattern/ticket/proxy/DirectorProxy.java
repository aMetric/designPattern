package com.coderwhs.designPattern.ticket.proxy;

import com.coderwhs.designPattern.ticket.director.AbstractDirector;
import com.coderwhs.designPattern.ticket.director.Director;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author whs
 * @Date 2024/7/27 16:37
 * @description: 静态代理类
 */
@Component
public class DirectorProxy extends AbstractDirector {

    //被代理角色
    @Autowired
    private Director director;

    /**
     * @param type personal代表个人电子发票，company代表企业电子发票
     * @param productId
     * @param content
     * @param title
     * @param bankInfo
     * @param taxId
     * @return
     */
    @Override
    public Object buildTicket(String type, String productId, String content, String title, String bankInfo, String taxId) {
        //前置处理，通过productId获取商品信息
        String product = this.getProduct(productId);
        //前置处理，校验银行卡信息
        if(bankInfo != null && !this.validateBankInfo(bankInfo)){
            return null;
        }
        return director.buildTicket(type, product, content, title, bankInfo, taxId);
    }

    //前置处理，通过productId获取商品信息
    private String getProduct(String productId){
        return "通过productId获取商品信息";
    }

    //前置处理，校验银行卡信息
    private boolean validateBankInfo(String bankInfo){
        System.out.println("银行卡校验逻辑");
        return true;
    }
}
