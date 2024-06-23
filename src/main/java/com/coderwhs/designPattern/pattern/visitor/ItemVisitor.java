package com.coderwhs.designPattern.pattern.visitor;

import com.coderwhs.designPattern.pattern.composite.AbstractProductItem;

/**
 * @Author whs
 * @Date 2024/6/22 10:38
 * @description: 抽象访问者-定义访问者可以访问的数据类型
 */
public interface ItemVisitor<T> {
    T visitor(AbstractProductItem productItem);
}
