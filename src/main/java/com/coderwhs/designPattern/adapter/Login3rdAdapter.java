package com.coderwhs.designPattern.adapter;

import com.alibaba.fastjson.JSONObject;
import com.coderwhs.designPattern.model.entity.UserInfo;
import com.coderwhs.designPattern.service.UserInfoService;
import com.coderwhs.designPattern.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author wuhs
 * @Date 2024/4/13 21:43
 * @Description 适配器
 */
@Component
// @Primary
public class Login3rdAdapter extends UserInfoService implements ILogin3rdTarget{

    @Value("${gitee.state}")
    private String giteeState;

    @Value("${gitee.token.url}")
    private String giteeTokenUrl;

    @Value("${gitee.user.url}")
    private String giteeUserUrl;

    @Value("${gitee.user.prefix}")
    private String giteeUserPrefix;

    @Override
    public String loginByGitee(String code, String state) {
        if (!giteeState.equals(state)){
            throw new UnsupportedOperationException("state error");
        }
        //请求gitee平台获取token，并携带code
        String tokenUrl = giteeTokenUrl.concat(code);
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
        if(super.checkUserExists(userName)){
            return super.login(userName,password);
        }

        //组装用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
        userInfo.setUserPassword(password);
        userInfo.setCreateDate(new Date());
        super.register(userInfo);
        return super.login(userName,password);
    }

    @Override
    public String loginByWechat(String... params) {
        return null;
    }

    @Override
    public String loginByQQ(String... params) {
        return null;
    }

    // @Resource
    // private UserInfoService userInfoService;

}
