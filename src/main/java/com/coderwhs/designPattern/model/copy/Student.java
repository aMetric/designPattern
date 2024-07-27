package com.coderwhs.designPattern.model.copy;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author whs
 * @Date 2024/7/27 14:09
 * @description:
 */
@Data
@AllArgsConstructor
public class Student implements Serializable {
    private String studentName;
}
