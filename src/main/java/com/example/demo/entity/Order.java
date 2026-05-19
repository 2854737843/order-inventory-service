package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long productId;

    private Integer count;

    private LocalDateTime createdAt;

    private String requestId;

    @TableField("req_hash")
    private String reqHash;

    @TableField("status")
    private Integer status; // 0 pending, 1 canceled
}