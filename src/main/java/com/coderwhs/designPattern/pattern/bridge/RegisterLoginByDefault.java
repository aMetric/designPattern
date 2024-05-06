package com.coderwhs.designPattern.pattern.bridge;

import com.coderwhs.designPattern.model.entity.UserInfo;
import com.coderwhs.designPattern.model.enums.LoginTypeEnum;
import com.coderwhs.designPattern.pattern.bridge.factory.RegisterLoginComponentFactory;
import com.coderwhs.designPattern.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author wuhs
 * @Date 2024/4/18 21:02
 * @Description 桥接模式-默认登入方式
 */
@Component
public class RegisterLoginByDefault extends AbstractRegisterLoginFunc implements IRegisterLoginFunc{

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    private void initFuncMap(){
        // this 是指当前对象的引用，即指向 RegisterLoginByDefault 对象的引用。this 关键字表示当前正在初始化的 RegisterLoginByDefault 对象本身。
        RegisterLoginComponentFactory.FUNC_MAP.put(LoginTypeEnum.DEFAULT.getValue(), this);
    }

    @Override
    public String login(String account, String password) {
        return super.commonLogin(account,password,userRepository);
    }

    @Override
    public String register(UserInfo userInfo) {
        return super.commonRegister(userInfo,userRepository);
    }

    @Override
    public boolean checkUserExists(String userName) {
        return super.checkUserExists(userName);
    }
}
