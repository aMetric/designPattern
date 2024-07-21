package com.coderwhs.designPattern.repo;

import com.coderwhs.designPattern.model.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Integer> {
    //根据product id查询商品信息
    public Products findByProductId(String productId);
}
