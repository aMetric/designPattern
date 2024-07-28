package com.coderwhs.designPattern.service.impl;

import com.coderwhs.designPattern.common.ErrorCode;
import com.coderwhs.designPattern.dutyChain.AbstractBusinessHandler;
import com.coderwhs.designPattern.dutyChain.CityHandler;
import com.coderwhs.designPattern.exception.ThrowUtils;
import com.coderwhs.designPattern.model.entity.BusinessLaunch;
import com.coderwhs.designPattern.model.entity.UserInfo;
import com.coderwhs.designPattern.model.enums.DutyChainHandlerEnum;
import com.coderwhs.designPattern.repo.BusinessLaunchRepository;
import com.coderwhs.designPattern.repo.UserRepository;
import com.coderwhs.designPattern.service.inter.UserInfoService;
import com.coderwhs.designPattern.ticket.proxy.DirectorProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    @Autowired
    private DirectorProxy directorProxy;

    @Autowired
    private BusinessLaunchRepository businessLaunchRepository;

    @Value("${duty.chain}")
    private String handlerType;

    // 记录当前handlerType的配置，判断duty.chain的配置是否有修改
    private String currentHandlerType;
    // 记录当前的责任链头节点，如果配置没有修改，下次直接返回即可
    private AbstractBusinessHandler currentHandler;

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

    /**
     * 业务投放过滤
     *
     * @param city
     * @param sex
     * @param product
     * @return
     */
    @Override
    public List<BusinessLaunch> filterBusinessLaunch(String city, String sex, String product) {
        List<BusinessLaunch> launchList = businessLaunchRepository.findAll();
        return Objects.requireNonNull(buildChain()).processHandler(launchList,city,sex,product);
    }

    /**
     * 开具电子发票
     *
     * @param type
     * @param productId
     * @param content
     * @param title
     * @param bankInfo
     * @param taxId
     * @return
     */
    @Override
    public Object createTicket(String type, String productId, String content, String title, String bankInfo, String taxId) {
        return directorProxy.buildTicket(type, productId, content, title, bankInfo, taxId);
    }

    /**
     * 组装责任链条并返回责任链条首节点
     * @return
     */
    private AbstractBusinessHandler buildChain(){
        // 如果没有配置，直接返回null
        if(handlerType == null){
            return null;
        }

        //如果是第一次配置，将handlerType记录下来
        if(currentHandlerType == null){
            this.currentHandlerType = this.handlerType;
        }

        //说明duty.chain的配置并未修改且才currentHandler不为null，直接返回currentHandler
        if(this.handlerType.equals(currentHandlerType) && this.currentHandler != null){
            return this.currentHandler;
        }else{
            //说明duty.chain的配置有修改，需要从新初始化责任链条
            //从新初始化责任链条，需要保证线程安全，仅仅每次修改配置才会执行一次此处代码，无性能问题
            synchronized (this){
                try {
                    //创建哑节点，随意找一个具体类型创建即可
                    AbstractBusinessHandler dummyHeadHandler = new CityHandler();

                    //创建前置节点，初始赋值为哑节点
                    AbstractBusinessHandler preHandler = dummyHeadHandler;

                    //将duty.chain的配置用逗号分割为list类型，并通过handlerEnum创建责任类，并配置责任链条
                    List<String> handlerTypeList = Arrays.asList(handlerType.split(","));
                    for(String handlerType : handlerTypeList){
                        AbstractBusinessHandler handler = (AbstractBusinessHandler)Class.forName(DutyChainHandlerEnum.valueOf(handlerType).getValue()).newInstance();
                        preHandler.nextHandler = handler;
                        preHandler = handler;
                    }
                    //重新赋值新的责任链条头节点
                    this.currentHandler = dummyHeadHandler.nextHandler;

                    //重新赋值修改后的配置
                    this.currentHandlerType = this.handlerType;

                    //返回责任链头结点
                    return currentHandler;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public boolean checkUserExists(String userName){
        UserInfo userInfo = userRepository.findByUserName(userName);
        if (userInfo == null){
            return false;
        }
        return true;
    }
}
