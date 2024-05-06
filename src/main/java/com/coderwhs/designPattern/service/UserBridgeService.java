package com.coderwhs.designPattern.service;

import com.coderwhs.designPattern.model.entity.UserInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author wuhs
 * @Date 2024/4/13 20:23
 * @Description 桥接模式实现第三方登入的调用位置
 */
public interface UserBridgeService {

    String login(String account, String password);

    String register(UserInfo userInfo);

    String login3rd(HttpServletRequest request,String type);
}
