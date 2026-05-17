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
    BAD_REQUEST(40000, "参数错误"),
    BIZ_ERROR(50001, "业务异常"),
    INTERNAL_ERROR(50000, "系统异常");

    private final int code;
    private final String defaultMessage;

    ErrorCode(int code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}
