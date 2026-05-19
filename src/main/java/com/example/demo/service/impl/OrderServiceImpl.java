package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.api.ErrorCode;
import com.example.demo.common.exception.BizException;
import com.example.demo.dto.OrderDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(ProductMapper productMapper, OrderMapper orderMapper) {
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
    }


    @Override
    @Transactional
    public Long createOrder(String requestId, Long productId, int count) {
        // 0) 计算本次请求的 hash（用于幂等一致性校验）
        String reqHash = sha256(productId + ":" + count);

        // 1) 幂等：先查 requestId 是否已存在
        Order existing = orderMapper.findByRequestId(requestId);
        if (existing != null) {
            String dbHash = existing.getReqHash();
            // 1.1 参数一致：返回同一订单
            if (Objects.equals(dbHash, reqHash)) {
                return existing.getId();
            }
            // 1.2 参数不一致：幂等冲突（必须报错）
            throw new BizException(ErrorCode.IDEMPOTENT_CONFLICT);
        }

        // 2) 商品存在
        Product p = productMapper.selectById(productId);
        if (p == null) {
            throw new BizException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        // 3) 原子扣库存
        int updated = productMapper.deductStock(productId, count);
        if (updated == 0) {
            throw new BizException(ErrorCode.STOCK_NOT_ENOUGH);
        }

        // 4) 写订单（带 requestId + reqHash）
        Order o = new Order();
        o.setRequestId(requestId);
        o.setReqHash(reqHash);
        o.setProductId(productId);
        o.setCount(count);
        o.setStatus(0);

        try {
            orderMapper.insert(o);
            return o.getId();
        } catch (Exception ex) {
            // 5) 并发重复 requestId：查出来再判定 hash
            Order again = orderMapper.findByRequestId(requestId);
            if (again != null) {
                if (Objects.equals(again.getReqHash(), reqHash)) return again.getId();
                throw new BizException(ErrorCode.IDEMPOTENT_CONFLICT);
            }
            throw ex;
        }
    }

    // 放在 OrderServiceImpl 内部即可
    private String sha256(String s) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(s.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<OrderDTO> pageWithProductName(int page, int size, Long productId) {
        Page<Order> mpPage = new Page<>(page, size);

        LambdaQueryWrapper<Order> qw = new LambdaQueryWrapper<Order>()
                .eq(productId != null, Order::getProductId, productId)
                .orderByDesc(Order::getId);

        Page<Order> orderPage = orderMapper.selectPage(mpPage, qw);

        // 1) 收集本页所有 productId
        List<Long> productIds = orderPage.getRecords().stream()
                .map(Order::getProductId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 2) 批量查商品并转成 Map
        Map<Long, Product> productMap = productIds.isEmpty()
                ? Collections.emptyMap()
                : productMapper.selectBatchIds(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        // 3) 组装 DTO records
        List<OrderDTO> dtoRecords = orderPage.getRecords().stream().map(o -> {
            OrderDTO dto = new OrderDTO();
            dto.setId(o.getId());
            dto.setProductId(o.getProductId());
            dto.setCount(o.getCount());
            dto.setCreatedAt(o.getCreatedAt());

            Product p = productMap.get(o.getProductId());
            dto.setProductName(p == null ? null : p.getName());
            return dto;
        }).toList();

        // 4) 构造 DTO Page（把分页元信息带过去）
        Page<OrderDTO> dtoPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        dtoPage.setRecords(dtoRecords);
        return dtoPage;
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order o = orderMapper.findForCancel(orderId);
        if (o == null) {
            throw new BizException(ErrorCode.ORDER_NOT_FOUND);
        }

        // 先尝试把订单从 PENDING 改为 CANCELED
        int changed = orderMapper.cancelIfPending(orderId);
        if (changed == 0) {
            // 说明要么不存在（上面已排除），要么已经取消过了
            // 取消接口做幂等：直接返回成功，不再回补库存
            return;
        }

        // changed==1：本次是第一次成功取消 -> 回补库存
        productMapper.addStock(o.getProductId(), o.getCount());
    }
}