package com.coderwhs.designPattern.composite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author wuhs
 * @Date 2024/6/11 7:39
 * @Description 创建composite树枝构件
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductComposite extends AbstractProductItem{
  private int id;
  private int pid;
  private String name;
  private List<AbstractProductItem> child = new ArrayList<>();

  //增加商品类目
  @Override
  public void addProductItem(AbstractProductItem item){
    this.child.add(item);
  }

  //删除商品类目
  @Override
  public void delProductChild(AbstractProductItem item){
    ProductComposite removeItem = (ProductComposite)item;

    Iterator<AbstractProductItem> iterator = child.iterator();

    while (iterator.hasNext()){
      ProductComposite composite = (ProductComposite)iterator.next();
      //移除ID相同的商品类目
      if(composite.getId() == removeItem.getId()){
        iterator.remove();
        break;
      }
    }
  }
}
