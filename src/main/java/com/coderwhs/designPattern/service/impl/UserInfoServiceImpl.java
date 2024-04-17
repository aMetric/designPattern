package com.coderwhs.designPattern.service.impl;

import com.coderwhs.designPattern.common.ErrorCode;
import com.coderwhs.designPattern.exception.ThrowUtils;
import com.coderwhs.designPattern.model.entity.UserInfo;
import com.coderwhs.designPattern.repo.UserRepository;
import com.coderwhs.designPattern.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @Author wuhs
 * @Date 2024/4/13 20:23
 * @Description
 */
// @Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public String login(String account, String password) {
        UserInfo userInfo = userRepository.findByUserNameAndUserPassword(account,password);
        ThrowUtils.throwIf(userInfo==null, ErrorCode.NOT_FOUND_ERROR);
        return "Login success";
    }

    @Override
    public String register(UserInfo userInfo) {
        ThrowUtils.throwIf(checkUserExists(userInfo.getUserName()), ErrorCode.SYSTEM_ERROR);
        userInfo.setCreateDate(new Date());
        userRepository.save(userInfo);
        return "success";
    }

    public boolean checkUserExists(String userName){
        UserInfo userInfo = userRepository.findByUserName(userName);
        if (userInfo == null){
            return false;
        }
        return true;
    }
}
