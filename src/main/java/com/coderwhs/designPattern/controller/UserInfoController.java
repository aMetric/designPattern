package com.coderwhs.designPattern.controller;

import com.coderwhs.designPattern.pattern.adapter.Login3rdAdapter;
import com.coderwhs.designPattern.model.entity.UserInfo;
import com.coderwhs.designPattern.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author wuhs
 * @Date 2024/4/13 20:25
 * @Description
 */
@RestController
@RequestMapping("/userInfo")
@Slf4j
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @Autowired
    private Login3rdAdapter login3rdAdapter;

    @GetMapping("/gitee")
    public String gitee(String code,String state){
        return login3rdAdapter.loginByGitee(code,state);
    }

    @PostMapping("/login")
    public String login(String account,String password){
        return userInfoService.login(account,password);
    }

    @PostMapping("/register")
    public String register(@RequestBody UserInfo userInfo){
        return userInfoService.register(userInfo);
    }
}
