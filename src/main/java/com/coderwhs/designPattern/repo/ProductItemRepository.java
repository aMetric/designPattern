package com.coderwhs.designPattern.repo;

import com.coderwhs.designPattern.model.entity.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author wuhs
 * @Date 2024/6/15 8:35
 * @Description 查询所有的商品类目信息
 */
@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Integer> {
    @Modifying
    @Query(value = "INSERT INTO PRODUCT_ITEM (id, name, pid) " +
            "values ((select max(id)+1 from PRODUCT_ITEM),?1,?2)", nativeQuery = true)
    public void addItem(@Param("name") String name, @Param("pid") int pid);

    @Modifying
    @Query(value = "DELETE FROM PRODUCT_ITEM WHERE " +
            "id=?1 or pid=?1", nativeQuery = true)
    public void delItem(@Param("id") int id);

    public ProductItem findByNameAndPid(@Param("name") String name, @Param("pid") int pid);
}

