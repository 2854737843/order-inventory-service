package com.example.demo.controller;

import com.example.demo.common.api.Result;
import com.example.demo.dto.CreateProductReq;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: DemoController
 * Package: com.example.demo.controller
 * Description:
 *
 * @Author 王川
 * @Create 2026/5/17 22:00
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/demo")
public class DemoController {

    @PostMapping("/product")
    public Result<CreateProductReq> createProduct(@RequestBody @Valid CreateProductReq req) {
        // 先不连数据库，先把校验+统一返回跑通
        return Result.ok(req);
    }

    @GetMapping("/hello")
    public Result<String> hello(@RequestParam(required = false) String name) {
        return Result.ok("hello " + (name == null ? "world" : name));
    }
}
