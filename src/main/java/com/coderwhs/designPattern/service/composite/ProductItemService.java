package com.coderwhs.designPattern.service.composite;

import com.coderwhs.designPattern.model.entity.ProductItem;
import com.coderwhs.designPattern.composite.ProductComposite;

/**
 * @Author wuhs
 * @Date 2024/6/15 8:47
 * @Description 商品类目service
 */
public interface ProductItemService {
  /**
   * 获取商品类目信息
   * @return
   */
  ProductComposite fatchAllItems();

  /**
   * 增加商品类目信息
   * @return
   */
  ProductComposite addItems(ProductItem item);

  /**
   * 删除商品类目信息
   * @return
   */
  ProductComposite delItems(ProductItem item);
}
