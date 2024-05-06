package com.coderwhs.designPattern.pattern.bridge;

import com.alibaba.fastjson.JSONObject;
import com.coderwhs.designPattern.model.entity.UserInfo;
import com.coderwhs.designPattern.model.enums.LoginTypeEnum;
import com.coderwhs.designPattern.pattern.bridge.factory.RegisterLoginComponentFactory;
import com.coderwhs.designPattern.repo.UserRepository;
import com.coderwhs.designPattern.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Author wuhs
 * @Date 2024/4/18 21:02
 * @Description 桥接模式-gitee登入方式
 */
@Component
public class RegisterLoginByGitee extends AbstractRegisterLoginFunc implements IRegisterLoginFunc{

    @Value("${gitee.state}")
    private String giteeState;

    @Value("${gitee.token.url}")
    private String giteeTokenUrl;

    @Value("${gitee.user.url}")
    private String giteeUserUrl;

    @Value("${gitee.user.prefix}")
    private String giteeUserPrefix;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    private void initFuncMap(){
        // this 是指当前对象的引用，即指向 RegisterLoginByGitee 对象的引用。
        RegisterLoginComponentFactory.FUNC_MAP.put(LoginTypeEnum.GITEE.getValue(), this);
    }

    @Override
    public String login3rd(HttpServletRequest request) {
        String state = request.getParameter("state");
        String code = request.getParameter("code");
        if (!giteeState.equals(state)){
            throw new UnsupportedOperationException("state error");
        }
        //请求gitee平台获取token，并携带code
        String tokenUrl = giteeTokenUrl.concat(code);
        System.out.println("===================tokenUrl = " + tokenUrl);
        JSONObject tokenResObj = HttpClientUtils.execute(tokenUrl, HttpMethod.POST);
        String token = String.valueOf(tokenResObj.get("access_token"));

        //请求用户信息，并携带token
        String userUrl = giteeUserUrl.concat(token);
        JSONObject userInfoObj = HttpClientUtils.execute(userUrl, HttpMethod.GET);

        //获取用户信息，userName添加前缀，密码保持与userName一致
        String userName = giteeUserPrefix.concat(String.valueOf(userInfoObj.get("name")));
        String password = userName;

        return autoRegister3rdAndLogin(userName,password);
    }

    /**
     * 自动登入方法
     * @param userName
     * @param password
     * @return
     */
    private String autoRegister3rdAndLogin(String userName,String password){
        //若第三方账户登入过，则直接登入
        if(super.commonCheckUserExists(userName,userRepository)){
            return super.commonLogin(userName,password,userRepository);
        }

        //组装用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
        userInfo.setUserPassword(password);
        userInfo.setCreateDate(new Date());

        super.commonRegister(userInfo,userRepository);

        return super.commonLogin(userName,password,userRepository);
    }
}
