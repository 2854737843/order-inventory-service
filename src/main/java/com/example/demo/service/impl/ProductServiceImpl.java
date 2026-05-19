package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.api.ErrorCode;
import com.example.demo.common.exception.BizException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ClassName: ProductServiceImpl
 * Package: com.example.demo.service.impl
 * Description:
 *
 * @Author 王川
 * @Create 2026/5/17 23:44
 * @Version 1.0
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public Product create(String name, int stock) {
        Product p = new Product();
        p.setName(name);
        p.setStock(stock);
        productMapper.insert(p);
        return p;
    }

    @Override
    public Product getById(Long id) {
        return productMapper.selectById(id);
    }


    @Override
    public Page<Product> page(int page, int size) {
        // 对外 page 从 1 开始，MyBatis-Plus 也是 1-based
        Page<Product> mpPage = new Page<>(page, size);
        LambdaQueryWrapper<Product> qw = new LambdaQueryWrapper<Product>()
                .orderByDesc(Product::getId);

        return productMapper.selectPage(mpPage, qw);
    }

    @Override
    @Transactional
    public void deductStock(Long id, int count) {
        // 先判断商品是否存在（错误更准确）
        Product p = productMapper.selectById(id);
        if (p == null) {
            throw new BizException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        int updated = productMapper.deductStock(id, count);
        if (updated == 0) {
            throw new BizException(ErrorCode.STOCK_NOT_ENOUGH);
        }
    }
}