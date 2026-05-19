package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Product;

/**
 * ClassName: ProductService
 * Package: com.example.demo.service
 * Description:
 *
 * @Author 王川
 * @Create 2026/5/17 23:26
 * @Version 1.0
 */
public interface ProductService {
    Product create(String name, int stock);
    Product getById(Long id);
    Page<Product> page(int page, int size);
    void deductStock(Long id, int count);
}