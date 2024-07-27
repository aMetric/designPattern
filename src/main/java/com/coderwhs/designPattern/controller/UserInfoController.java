package com.coderwhs.designPattern.controller;

import com.coderwhs.designPattern.adapter.Login3rdAdapter;
import com.coderwhs.designPattern.common.BaseResponse;
import com.coderwhs.designPattern.common.ResultUtils;
import com.coderwhs.designPattern.model.entity.BusinessLaunch;
import com.coderwhs.designPattern.model.entity.UserInfo;
import com.coderwhs.designPattern.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author wuhs
 * @Date 2024/4/13 20:25
 * @Description
 */
@RestController
@RequestMapping("/user")
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

    @PostMapping("/business/launch")
    public BaseResponse<List<BusinessLaunch>> filterBusinessLaunch(@RequestParam("city") String city,
                                                                   @RequestParam("sex")String sex,
                                                                   @RequestParam("product")String product){
        return ResultUtils.success(userInfoService.filterBusinessLaunch(city, sex, product));
    }

    @PostMapping("/ticket")
    public BaseResponse<Object> createTicket(String type, String productId, String content, String title, String bankInfo, String taxId){
        return ResultUtils.success(userInfoService.createTicket(type, productId, content, title, bankInfo, taxId));
    }
}
