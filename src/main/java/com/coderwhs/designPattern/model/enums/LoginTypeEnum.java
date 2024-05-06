package com.coderwhs.designPattern.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 登入类型枚举
 *
 * @author coderwhs
 */
public enum LoginTypeEnum {

    DEFAULT("默认","DEAULT"),

    GITEE("Gitee","GITEE"),

    QQ("QQ","QQ"),

    WX("WX","WX");


    private final String text;

    private final String value;

    LoginTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static LoginTypeEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (LoginTypeEnum anEnum : LoginTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
