package com.coderwhs.designPattern.bridge;

import com.coderwhs.designPattern.model.entity.UserInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author wuhs
 * @Date 2024/4/20 10:45
 * @Description 抽象方法接口-client端调用方法入口
 */
public abstract class AbstractRegisterLoginComponent {

    //引入桥梁
    protected IRegisterLoginFunc iRegisterLoginFunc;

    public AbstractRegisterLoginComponent(IRegisterLoginFunc param){
        validate(param);
        this.iRegisterLoginFunc = param;
    }

    //声明为final，子类不可重写
    protected final void validate(IRegisterLoginFunc param){
        if(param == null){
            throw new UnsupportedOperationException("unsupport login/register function type!");
        }
    }

    public abstract String login(String userName,String password);
    public abstract String register(UserInfo userInfo);
    public abstract boolean checkUserExists(String userName);
    public abstract String login3rd(HttpServletRequest request);
}
