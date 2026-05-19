package com.example.demo.web;

import com.example.demo.common.api.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.UUID;

@RestControllerAdvice
public class ResultResponseAdvice implements ResponseBodyAdvice<Object> {

    private final HttpServletRequest request;

    public ResultResponseAdvice(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return Result.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest req,
            ServerHttpResponse res
    ) {
        if (body instanceof Result<?> r) {
            String requestId = headerOrNew("X-Request-Id");
            String traceId = headerOrNew("X-Trace-Id");

            r.setRequestId(requestId);
            r.setTraceId(traceId);
        }
        return body;
    }

    private String headerOrNew(String headerName) {
        String v = request.getHeader(headerName);
        if (v == null || v.isBlank()) {
            v = UUID.randomUUID().toString().replace("-", "");
        }
        return v;
    }
}