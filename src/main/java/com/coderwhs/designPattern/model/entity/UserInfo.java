package com.coderwhs.designPattern.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author wuhs
 * @Date 2024/4/13 17:53
 * @Description
 */
@Data
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String userPassword;

    @Column(nullable = false)
    private Date createDate;

    @Column
    private String userEmail;
}
