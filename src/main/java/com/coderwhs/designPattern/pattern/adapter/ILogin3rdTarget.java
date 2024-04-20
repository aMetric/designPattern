package com.coderwhs.designPattern.pattern.adapter;

/**
 * @Author wuhs
 * @Date 2024/4/13 21:43
 * @Description
 */
public interface ILogin3rdTarget {
    public String loginByGitee(String code, String state);
    public String loginByWechat(String ... params);
    public String loginByQQ(String ... params);
}
