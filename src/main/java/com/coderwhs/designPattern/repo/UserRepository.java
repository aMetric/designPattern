package com.coderwhs.designPattern.repo;

import com.coderwhs.designPattern.model.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author wuhs
 * @Date 2024/4/13 18:04
 * @Description 查询所有用户信息
 */
@Repository
public interface UserRepository extends JpaRepository<UserInfo,Integer> {

    //根据用户名查询用户信息
    UserInfo findByUserName(String userName);

    //根据用户名和密码查询用户信息
    UserInfo findByUserNameAndUserPassword(String account,String userPassword);
}
