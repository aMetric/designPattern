package com.coderwhs.designPattern.service;

import com.coderwhs.designPattern.model.entity.UserInfo;

/**
 * @Author wuhs
 * @Date 2024/4/13 20:23
 * @Description
 */
public interface UserInfoServiceTest {
    String login(String account, String password);

    String register(UserInfo userInfo);
}
