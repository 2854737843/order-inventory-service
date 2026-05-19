package com.example.demo.common.api;

import lombok.Getter;

/**
 * ClassName: ErrorCode
 * Package: com.example.demo.common.api
 * Description:
 *  错误码枚举
 * @Author 王川
 * @Create 2026/5/17 21:45
 * @Version 1.0
 */
@Getter
public enum ErrorCode {

    // 通用
    BAD_REQUEST(40001, "请求错误"),
    PARAM_INVALID(40000, "参数错误"),
    IDEMPOTENT_CONFLICT(40009, "幂等冲突：requestId已使用且参数不一致"),
    BIZ_ERROR(50001, "业务异常"),
    INTERNAL_ERROR(50000, "系统异常"),
    PRODUCT_NOT_FOUND(50002, "商品不存在"),
    STOCK_NOT_ENOUGH(50003, "库存不足"),
    ORDER_NOT_FOUND(50004, "订单不存在"),
    OK(0, "OK");

    private final int code;
    private final String defaultMessage;

    ErrorCode(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}
