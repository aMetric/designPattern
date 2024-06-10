package com.coderwhs.designPattern.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "products")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false, name = "product_id")
    private String productId;
    @Column(nullable = false, name = "send_red_bag")
    private Integer sendRedBag;
}
