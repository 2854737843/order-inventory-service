package com.example.demo.common.exception;

import com.example.demo.common.api.ErrorCode;
import com.example.demo.common.api.Result;
import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * ClassName: GlobalExceptionHander
 * Package: com.example.demo.common.exception
 * Description:
 * 全局异常处理
 * @Author 王川
 * @Create 2026/5/17 21:46
 * @Version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // DTO @Valid 校验失败（JSON body）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));
        return Result.fail(ErrorCode.BAD_REQUEST.getCode(), msg);
    }

    // 表单绑定校验失败
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        String msg = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));
        return Result.fail(ErrorCode.BAD_REQUEST.getCode(), msg);
    }

    // 单参数校验失败（@Validated + @Min 这类）
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolation(ConstraintViolationException e) {
        return Result.fail(ErrorCode.BAD_REQUEST.getCode(), e.getMessage());
    }

    // 业务异常
    @ExceptionHandler(BizException.class)
    public Result<Void> handleBizException(BizException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    // 兜底异常
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        // 先不打日志也行；等Day4再统一做日志规范
        e.printStackTrace();  // 把真实异常打出来
        return Result.fail(ErrorCode.INTERNAL_ERROR.getCode(), ErrorCode.INTERNAL_ERROR.getDefaultMessage());
    }

    private String formatFieldError(FieldError fe) {
        if (fe == null) return "参数错误";
        return fe.getField() + ": " + fe.getDefaultMessage();
    }
}