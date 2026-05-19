package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.api.Result;
import com.example.demo.common.exception.BizException;
import com.example.demo.dto.CreateOrderReq;
import com.example.demo.dto.OrderDTO;
import com.example.demo.common.api.ErrorCode;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Result<Long> create(
            @RequestHeader(value = "X-Request-Id", required = false) String requestId,
            @RequestBody @Valid CreateOrderReq req
    ) {
        // 允许为空：若客户端不传，就生成一个（和 Advice 的规则一致）
        if (requestId == null || requestId.isBlank()) {
            requestId = java.util.UUID.randomUUID().toString().replace("-", "");
        }
        Long orderId = orderService.createOrder(requestId, req.getProductId(), req.getCount());
        return Result.ok(orderId);
    }

    @GetMapping
    public Result<Page<OrderDTO>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long productId
    ) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        if (size > 100) size = 100;

        return Result.ok(orderService.pageWithProductName(page, size, productId));
    }

    /**
     * 取消订单（幂等）：第一次取消会回补库存；重复取消不会重复回补。
     *
     * 因为类上已经是 /api/order，所以这里写 /{id}/cancel 即可
     * 最终路径：POST /api/order/{id}/cancel
     */
    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable("id") Long id) {
        orderService.cancelOrder(id);
        return Result.ok(null);
    }
}