package com.coderwhs.designPattern.repo;

import com.coderwhs.designPattern.model.entity.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author wuhs
 * @Date 2024/6/15 8:35
 * @Description 查询所有的商品类目信息
 */
@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem,Integer> {

}
