package com.example.demo.web;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class RequestContext {

    private static final String HEADER_REQUEST_ID = "X-Request-Id";
    private static final String HEADER_TRACE_ID = "X-Trace-Id";

    public static String requestId() {
        HttpServletRequest req = currentRequest();
        if (req == null) return null;

        // 先取规范写法，取不到再兼容小写
        String v = req.getHeader(HEADER_REQUEST_ID);
        if (v == null || v.isBlank()) v = req.getHeader("x-request-id");
        return v;
    }

    public static String traceId() {
        HttpServletRequest req = currentRequest();
        if (req == null) return null;

        String v = req.getHeader(HEADER_TRACE_ID);
        if (v == null || v.isBlank()) v = req.getHeader("x-trace-id");
        return v;
    }

    private static HttpServletRequest currentRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs == null ? null : attrs.getRequest();
    }
}