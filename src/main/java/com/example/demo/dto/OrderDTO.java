package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Integer count;
    private LocalDateTime createdAt;
}