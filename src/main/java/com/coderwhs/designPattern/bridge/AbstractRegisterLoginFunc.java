package com.coderwhs.designPattern.bridge;

import com.coderwhs.designPattern.model.entity.UserInfo;
import com.coderwhs.designPattern.repo.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Author wuhs
 * @Date 2024/4/20 13:56
 * @Description 抽象中间层
 */
public abstract class AbstractRegisterLoginFunc implements IRegisterLoginFunc{

    protected String commonLogin(String account, String password, UserRepository userRepository) {
        UserInfo userInfo = userRepository.findByUserNameAndUserPassword(account, password);
        if(userInfo == null) {
            return "account / password ERROR!";
        }
        return "Login Success";
    }

    protected String commonRegister(UserInfo userInfo, UserRepository userRepository) {
        if(commonCheckUserExists(userInfo.getUserName(), userRepository)) {
            throw new RuntimeException("User already registered.");
        }
        userInfo.setCreateDate(new Date());
        userRepository.save(userInfo);
        return "Register Success!";
    }

    protected boolean commonCheckUserExists(String userName, UserRepository userRepository) {
        UserInfo user = userRepository.findByUserName(userName);
        if(user == null) {
            return false;
        }
        return true;
    }

    public String login(String account, String password){
        throw new UnsupportedOperationException();
    }

    public String register(UserInfo userInfo){
        throw new UnsupportedOperationException();
    }

    public boolean checkUserExists(String userName){
        throw new UnsupportedOperationException();
    }

    public String login3rd(HttpServletRequest request){
        throw new UnsupportedOperationException();
    }
}
