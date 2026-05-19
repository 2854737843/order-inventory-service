package com.example.demo.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: Result
 * Package: com.example.demo.common.api
 * Description:
 * 统一返回体
 * @Author 王川
 * @Create 2026/5/17 21:44
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private int code;
    private String message;
    private T data;

    // 新增：用于链路排查
    private String requestId;
    private String traceId;

    public static <T> Result<T> ok(T data) {
        return new Result<>(0, "OK", data, null, null);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null, null, null);
    }
}