package com.coderwhs.designPattern.bridge;

import com.coderwhs.designPattern.model.entity.UserInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author wuhs
 * @Date 2024/4/17 21:54
 * @Description
 */
public interface IRegisterLoginFunc {
    public String login(String account, String password);
    public String register(UserInfo userInfo);
    public boolean checkUserExists(String userName);
    public String login3rd(HttpServletRequest request);
}
