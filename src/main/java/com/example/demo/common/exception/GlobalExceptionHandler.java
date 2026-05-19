package com.example.demo.common.exception;


import com.example.demo.common.api.ErrorCode;
import com.example.demo.common.api.Result;
import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
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

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // DTO @Valid 校验失败（JSON body）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ":" + (fe.getDefaultMessage() == null ? "参数错误" : fe.getDefaultMessage()))
                .collect(Collectors.joining("; "));
        return Result.fail(ErrorCode.PARAM_INVALID.getCode(), "参数错误: " + msg);
    }

    // 表单绑定校验失败
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        String msg = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));
        return Result.fail(ErrorCode.PARAM_INVALID.getCode(), "参数错误: " + msg);
    }

    // 单参数校验失败（@Validated + @Min 这类）
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolation(ConstraintViolationException e) {
        return Result.fail(ErrorCode.PARAM_INVALID.getCode(), "参数错误: " + e.getMessage());
    }

    // 业务异常
    @ExceptionHandler(BizException.class)
    public Result<Void> handleBizException(BizException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    // 兜底异常
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        // 打日志：对内保留堆栈，对外只给统一提示
        String requestId = com.example.demo.web.RequestContext.requestId();
        String traceId = com.example.demo.web.RequestContext.traceId();

        log.error("Unhandled exception, requestId={}, traceId={}", requestId, traceId, e);

        return Result.fail(ErrorCode.INTERNAL_ERROR.getCode(), ErrorCode.INTERNAL_ERROR.getDefaultMessage());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public Result<Void> handleMissingRequestHeader(MissingRequestHeaderException e) {
        return Result.fail(ErrorCode.PARAM_INVALID.getCode(), "参数错误: 缺少请求头 " + e.getHeaderName());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        return Result.fail(ErrorCode.PARAM_INVALID.getCode(), "参数错误: body不是合法JSON");
    }

    private String formatFieldError(FieldError fe) {
        if (fe == null) return "参数错误";
        return fe.getField() + ": " + fe.getDefaultMessage();
    }
}