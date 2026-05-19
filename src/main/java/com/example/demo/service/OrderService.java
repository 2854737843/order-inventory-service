package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.dto.OrderDTO;

public interface OrderService {

    Long createOrder(String requestId, Long productId, int count);
    Page<OrderDTO> pageWithProductName(int page, int size, Long productId);
    void cancelOrder(Long orderId);
}