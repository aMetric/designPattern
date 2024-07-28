package com.coderwhs.designPattern.service.impl;

import com.coderwhs.designPattern.model.entity.UserInfo;
import com.coderwhs.designPattern.model.enums.LoginTypeEnum;
import com.coderwhs.designPattern.bridge.AbstractRegisterLoginComponent;
import com.coderwhs.designPattern.bridge.factory.RegisterLoginComponentFactory;
import com.coderwhs.designPattern.service.inter.UserBridgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author wuhs
 * @Date 2024/4/13 20:23
 * @Description
 */
@Service
@Slf4j
public class UserBridgeServiceImpl implements UserBridgeService {

    @Override
    public String login(String account, String password) {
        AbstractRegisterLoginComponent component = RegisterLoginComponentFactory.getComponent(LoginTypeEnum.DEFAULT.getValue());
        return component.login(account,password);
    }

    @Override
    public String register(UserInfo userInfo) {
        AbstractRegisterLoginComponent component = RegisterLoginComponentFactory.getComponent(LoginTypeEnum.DEFAULT.getValue());
        return component.register(userInfo);
    }

    @Override
    public String login3rd(HttpServletRequest request,String type) {
        AbstractRegisterLoginComponent component = RegisterLoginComponentFactory.getComponent(type);
        return component.login3rd(request);
    }
}
