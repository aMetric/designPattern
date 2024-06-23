package com.coderwhs.designPattern.pattern.composite;

/**
 * @Author wuhs
 * @Date 2024/6/11 7:19
 * @Description 创建component抽象组件
 *
 *  * 在这里，使用AbstractProductItem作为参数是为了确保参数的多样性和灵活性，从而遵循里氏替换原则。
 * 具体来说，子类可以是AbstractProductItem的任意子类，且子类的方法可以接受任意AbstractProductItem对象作为参数，从而确保代码的可扩展性和灵活性。
 * 同时，如果需要在具体的子类中对addProductItem和delProductItem方法进行具体的实现，也可以实现方法的重写，进一步遵循里氏替换原则。
 */
public abstract class AbstractProductItem {

  //增加商品类目
  protected void addProductItem(AbstractProductItem item){
    throw new UnsupportedOperationException("not support item add!");
  }

  //删除商品类目
  protected void delProductChild(AbstractProductItem item){
    throw new UnsupportedOperationException("not support item delete!");
  }
}
