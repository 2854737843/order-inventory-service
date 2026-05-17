package com.example.demo.common.exception;

import com.example.demo.common.api.ErrorCode;
import lombok.Getter;

/**
 * ClassName: BizException
 * Package: com.example.demo.common.exception
 * Description:
 * 业务异常
 * @Author 王川
 * @Create 2026/5/17 21:45
 * @Version 1.0
 */
@Getter
public class BizException extends RuntimeException {

    private final int code;

    public BizException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.code = errorCode.getCode();
    }

    public BizException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public static BizException of(ErrorCode errorCode) {
        return new BizException(errorCode);
    }

    public static BizException of(ErrorCode errorCode, String message) {
        return new BizException(errorCode, message);
    }
}
