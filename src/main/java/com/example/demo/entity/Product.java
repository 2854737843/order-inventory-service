package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ClassName: Product
 * Package: com.example.demo.entity
 * Description:
 *
 * @Author 王川
 * @Create 2026/5/17 23:22
 * @Version 1.0
 */
@Data
@TableName("product")
public class Product {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer stock;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}