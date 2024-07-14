package com.coderwhs.designPattern.service.impl.composite;

import com.coderwhs.designPattern.common.ErrorCode;
import com.coderwhs.designPattern.exception.ThrowUtils;
import com.coderwhs.designPattern.model.entity.ProductItem;
import com.coderwhs.designPattern.composite.AbstractProductItem;
import com.coderwhs.designPattern.composite.ProductComposite;
import com.coderwhs.designPattern.visitor.AddItemVisitor;
import com.coderwhs.designPattern.visitor.DelItemVisitor;
import com.coderwhs.designPattern.repo.ProductItemRepository;
import com.coderwhs.designPattern.service.composite.ProductItemService;
import com.coderwhs.designPattern.utils.CommonUtils;
import com.coderwhs.designPattern.utils.RedisCommonProcessor;
import com.coderwhs.designPattern.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author wuhs
 * @Date 2024/6/15 8:47
 * @Description 商品类目服务类Impl
 */
@Service
@Transactional
public class ProductItemServiceImpl implements ProductItemService {

  @Autowired
  private RedisCommonProcessor redisCommonProcessor;

  @Autowired
  private ProductItemRepository productItemRepository;

  @Autowired
  private AddItemVisitor addItemVisitor;

  @Autowired
  private DelItemVisitor delItemVisitor;

  /**
   * 获取商品类目信息
   * @return
   */
  @Override
  public ProductComposite fatchAllItems() {
    //先查询redis，如果不为null，直接返回
    Object cacheItems = redisCommonProcessor.get(RedisKeyUtils.PRODUCT_ITEM_KEY);
    if(cacheItems != null){
      return (ProductComposite) cacheItems;
    }

    //如果redis缓存为null，则查询DB
    List<ProductItem> productItemList = productItemRepository.findAll();

    //将商品信息封装成组合模式的树形结构
    ProductComposite items = genProductTree(productItemList);
    ThrowUtils.throwIf(CommonUtils.objectIsNull(items),ErrorCode.NOT_FOUND_ERROR);

    //写到redis
    redisCommonProcessor.set(RedisKeyUtils.PRODUCT_ITEM_KEY,items);

    return items;
  }

  /**
   * 增加商品类目信息
   *
   * @return
   */
  @Override
  public ProductComposite addItems(ProductItem item) {
    //先更新数据库
    productItemRepository.addItem(item.getName(),item.getPid());

    //通过访问者模式访问树形数据结构，并添加新的商品类目
    ProductComposite addItem = ProductComposite.builder()
            .id(productItemRepository.findByNameAndPid(item.getName(), item.getPid()).getId())
            .name(item.getName())
            .pid(item.getPid())
            .child(new ArrayList<>())
            .build();
    AbstractProductItem updatedItems = addItemVisitor.visitor(addItem);

    //更新redis缓存，此处可以做重试机制(使用MQ)，如果重试不成功可以人工介入
    redisCommonProcessor.set(RedisKeyUtils.PRODUCT_ITEM_KEY,updatedItems);

    return (ProductComposite) updatedItems;
  }

  /**
   * 删除商品类目信息
   *
   * @return
   */
  @Override
  public ProductComposite delItems(ProductItem item) {
    //先更新数据库
    productItemRepository.delItem(item.getId());

    //通过访问者模式访问树形结构，并删除商品类目
    ProductComposite delItem = ProductComposite.builder()
            .id(item.getId())
            .name(item.getName())
            .pid(item.getPid())
            .build();
    AbstractProductItem updatedItems = delItemVisitor.visitor(delItem);

    //更新redis缓存，此处可以做重试机制(使用MQ)，如果重试不成功可以人工介入
    redisCommonProcessor.set(RedisKeyUtils.PRODUCT_ITEM_KEY,updatedItems);

    return (ProductComposite) updatedItems;
  }

  /**
   * 组装成树形结构
   *
   * @param fetchDbItems
   * @return
   */
  private ProductComposite genProductTree(List<ProductItem> fetchDbItems) {
    List<ProductComposite> composites = new ArrayList<>(fetchDbItems.size());

    //初始化composites
    fetchDbItems.forEach(dbItem -> composites.add(ProductComposite.builder()
            .id(dbItem.getId())
            .name(dbItem.getName())
            .pid(dbItem.getPid())
            .build()));

    //将ProductComposite按照其父ID（pid）进行分组，形成一个映射（groupingList），以便之后能够快速找到每个节点的子节点。
    Map<Integer, List<ProductComposite>> groupingList =
            composites.stream().collect(Collectors.groupingBy(ProductComposite::getPid));

    //遍历ProductComposite列表，将每个节点的子节点列表设置为其子节点的ProductComposite对象列表。
    composites.forEach(item -> {
      //找到当前节点的子节点
      List<ProductComposite> list = groupingList.get(item.getId());
      item.setChild(list == null ? new ArrayList<>() : list.stream().map(x -> (AbstractProductItem)x).collect(Collectors.toList()));
    });

    return composites.isEmpty() ? null : composites.get(0);
  }
}
