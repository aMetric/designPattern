package com.coderwhs.designPattern.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author wuhs
 * @Date 2024/6/15 12:08
 * @Description 公共工具类
 */
public class CommonUtils {

  /**
   * 判断对象是否不为空(支持String、list、map 判断是否为空字符串、size等于零)
   * @param object
   * @return
   */
  public static boolean objectIsNotNull(Object object){
    return !objectIsNull(object);
  }

  public static boolean objectIsNull(Object object) {

    /*
     * 判断对象是否为空
     */
    if (object == null) {
      return true;
    }

    /*
     * 判断对象是否 List类型 如果是判断size大小
     */
    if (object instanceof List) {
      if (((List) object).size() <= 0) {
        return true;
      }
    }

    /*
     * 判断对象是否 Map类型 如果是判断size大小
     */
    if (object instanceof Map) {
      if (((Map) object).size() <= 0) {
        return true;
      }
    }

    /*
     * 判断对象是否 Set类型 如果是判断size大小
     */
    if (object instanceof Set) {
      if (((Set) object).size() <= 0) {
        return true;
      }
    }

    /*
     * 判断对象是否String 类型
     */
    if (object instanceof String) {
      if("null".equals(object)){
        return true;
      }
      return ((String) object).isEmpty();
    }

    return false;
  }

}
