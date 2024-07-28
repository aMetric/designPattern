package com.coderwhs.designPattern.controller;


import com.coderwhs.designPattern.model.entity.UserInfo;
import com.coderwhs.designPattern.service.inter.UserBridgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author wuhs
 * @Date 2024/4/13 20:25
 * @Description 桥接模式实现登入
 */
@RestController
@RequestMapping("/bridge")
@Slf4j
public class UserBridgeController {
    @Resource
    private UserBridgeService userBridgeService;

    @GetMapping("/gitee")
    public String login3rd(HttpServletRequest request,String type){
        return userBridgeService.login3rd(request,type);
    }

    @PostMapping("/login")
    public String login(String account,String password){
        return userBridgeService.login(account,password);
    }

    @PostMapping("/register")
    public String register(@RequestBody UserInfo userInfo){
        return userBridgeService.register(userInfo);
    }
}
