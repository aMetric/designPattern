package com.coderwhs.designPattern.service.composite;

import com.coderwhs.designPattern.model.entity.ProductItem;
import com.coderwhs.designPattern.pattern.composite.ProductComposite;

/**
 * @Author wuhs
 * @Date 2024/6/15 8:47
 * @Description 查询商品类目service
 */
public interface ProductItemService {
  //获取商品类目信息
  ProductComposite fatchAllItems();
}
