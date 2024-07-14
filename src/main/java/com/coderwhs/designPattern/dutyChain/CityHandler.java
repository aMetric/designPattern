package com.coderwhs.designPattern.dutyChain;

import com.coderwhs.designPattern.model.entity.BusinessLaunch;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author whs
 * @Date 2024/7/14 19:16
 * @description: 按用户所在城市种类的筛选责任类
 */
public class CityHandler extends AbstractBusinessHandler{
    @Override
    public List<BusinessLaunch> processHandler(List<BusinessLaunch> launchList, String targetCity, String targetSex, String targetProduct) {
        if(launchList.isEmpty()){
            return launchList;
        }

        // 按target进行筛选，只保留符合条件的投放信息
        launchList = launchList.stream().filter(launch -> {
            String city = launch.getTargetCity();
            if (StringUtils.isEmpty(city)) {
                return true;
            }
            List<String> cityList = Arrays.asList(city.split(","));
            return cityList.contains(targetCity);
        }).collect(Collectors.toList());

        // 如果还有下一个责任类，则继续进行筛选
        if(hasNextHandler()){
            return nextHandler.processHandler(launchList,targetCity,targetSex,targetProduct);
        }
        return launchList;
    }
}
