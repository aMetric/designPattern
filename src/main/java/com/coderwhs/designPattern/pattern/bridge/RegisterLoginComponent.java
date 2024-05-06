package com.coderwhs.designPattern.pattern.bridge;

import com.coderwhs.designPattern.model.entity.UserInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author wuhs
 * @Date 2024/4/20 10:53
 * @Description 抽象方法接口的实现类
 */
public class RegisterLoginComponent extends AbstractRegisterLoginComponent {

    //通过构造函数，传入桥梁
    public RegisterLoginComponent(IRegisterLoginFunc param) {
        super(param);
    }

    @Override
    public String login(String userName, String password) {
        return iRegisterLoginFunc.login(userName,password);
    }

    @Override
    public String register(UserInfo userInfo) {
        return iRegisterLoginFunc.register(userInfo);
    }

    @Override
    public boolean checkUserExists(String userName) {
        return iRegisterLoginFunc.checkUserExists(userName);
    }

    @Override
    public String login3rd(HttpServletRequest request) {
        return iRegisterLoginFunc.login3rd(request);
    }
}
