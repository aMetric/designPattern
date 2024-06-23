package com.coderwhs.designPattern.controller;

import com.coderwhs.designPattern.common.BaseResponse;
import com.coderwhs.designPattern.common.ResultUtils;
import com.coderwhs.designPattern.model.entity.ProductItem;
import com.coderwhs.designPattern.pattern.composite.ProductComposite;
import com.coderwhs.designPattern.service.composite.ProductItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author wuhs
 * @Date 2024/6/15 16:51
 * @Description 商品类目查询接口
 */
@RestController
@RequestMapping("/product")
@Slf4j
public class ProductItemController {

  @Autowired
  private ProductItemService productItemService;

  /**
   * 获取商品类目信息
   */
  @PostMapping("/fetchAllItems")
  public BaseResponse<ProductComposite> fetchAllItems(){
    ProductComposite composite = productItemService.fatchAllItems();
    return ResultUtils.success(composite);
  }

  @PostMapping("/addItems")
  public ProductComposite addItems(@RequestBody ProductItem item){
    return productItemService.addItems(item);
  }

  @PostMapping("/delItems")
  public ProductComposite delItems(@RequestBody ProductItem item){
    return productItemService.delItems(item);
  }
}
