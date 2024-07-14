package com.coderwhs.designPattern.repo;

import com.coderwhs.designPattern.model.entity.BusinessLaunch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author whs
 * @Date 2024/7/14 15:51
 * @description: 业务投放类repo
 */
@Repository
public interface BusinessLaunchRepository extends JpaRepository<BusinessLaunch, Integer> {
}
