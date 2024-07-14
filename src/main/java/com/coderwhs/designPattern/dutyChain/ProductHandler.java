package com.coderwhs.designPattern.dutyChain;

import com.coderwhs.designPattern.model.entity.BusinessLaunch;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author whs
 * @Date 2024/7/14 19:17
 * @description: 按用户购物种类的筛选责任类
 */
public class ProductHandler extends AbstractBusinessHandler{
    @Override
    public List<BusinessLaunch> processHandler(List<BusinessLaunch> launchList, String targetCity, String targetSex, String targetProduct) {
        if(launchList.isEmpty()){
            return launchList;
        }

        // 按target进行筛选，只保留符合条件的投放信息
        launchList = launchList.stream().filter(launch -> {
            String product = launch.getTargetProduct();
            if (StringUtils.isEmpty(product)) {
                return true;
            }
            List<String> cityList = Arrays.asList(product.split(","));
            return cityList.contains(targetProduct);
        }).collect(Collectors.toList());

        // 如果还有下一个责任类，则继续进行筛选
        if(hasNextHandler()){
            return nextHandler.processHandler(launchList,targetCity,targetSex,targetProduct);
        }
        return launchList;
    }
}
