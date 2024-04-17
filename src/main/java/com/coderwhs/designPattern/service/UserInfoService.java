package com.coderwhs.designPattern.service;

import com.coderwhs.designPattern.common.ErrorCode;
import com.coderwhs.designPattern.exception.ThrowUtils;
import com.coderwhs.designPattern.model.entity.UserInfo;
import com.coderwhs.designPattern.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author wuhs
 * @Date 2024/4/17 20:59
 * @Description
 */
@Service
public class UserInfoService {
    @Autowired
    private UserRepository userRepository;

    public String login(String account, String password) {
        UserInfo userInfo = userRepository.findByUserNameAndUserPassword(account,password);
        ThrowUtils.throwIf(userInfo==null, ErrorCode.NOT_FOUND_ERROR);
        return "Login success";
    }

    // @Override
    public String register(UserInfo userInfo) {
        ThrowUtils.throwIf(checkUserExists(userInfo.getUserName()), ErrorCode.SYSTEM_ERROR);
        userInfo.setCreateDate(new Date());
        userRepository.save(userInfo);
        return "register success";
    }

    public boolean checkUserExists(String userName){
        UserInfo userInfo = userRepository.findByUserName(userName);
        if (userInfo == null){
            return false;
        }
        return true;
    }
}
