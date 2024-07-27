package com.coderwhs.designPattern.service;

import com.coderwhs.designPattern.model.entity.BusinessLaunch;
import com.coderwhs.designPattern.model.entity.UserInfo;

import java.util.List;

/**
 * @Author wuhs
 * @Date 2024/4/13 20:23
 * @Description
 */
public interface UserInfoService {
    String login(String account, String password);

    String register(UserInfo userInfo);

    /**
     * 业务投放过滤
     * @param city
     * @param sex
     * @param product
     * @return
     */
    List<BusinessLaunch> filterBusinessLaunch(String city,String sex,String product);

    /**
     * 开具电子发票
     * @param type
     * @param productId
     * @param content
     * @param title
     * @param bankInfo
     * @param taxId
     * @return
     */
    public Object createTicket(String type,String productId,String content,String title,String bankInfo,String taxId);
}
