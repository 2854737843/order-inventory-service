package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.api.Result;
import com.example.demo.dto.CreateProductDBReq;
import com.example.demo.dto.DeductStockReq;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: ProductController
 * Package: com.example.demo.controller
 * Description:
 *
 * @Author 王川
 * @Create 2026/5/17 23:46
 * @Version 1.0
 */

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Result<Product> create(@RequestBody @Valid CreateProductDBReq req) {
        Product p = productService.create(req.getName(), req.getStock() == null ? 0 : req.getStock());
        return Result.ok(p);
    }

    @GetMapping("/{id}")
    public Result<Product> get(@PathVariable Long id) {
        return Result.ok(productService.getById(id));
    }

    @GetMapping
    public Result<Page<Product>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        if (size > 100) size = 100;

        return Result.ok(productService.page(page, size));
    }

    @PostMapping("/{id}/deduct")
    public Result<Void> deduct(@PathVariable Long id, @RequestBody @Valid DeductStockReq req) {
        productService.deductStock(id, req.getCount());
        return Result.ok(null);
    }
}
