package com.example.demo.common.api;

public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    // 为了排查问题（你请求头里已经有 x-request-id / x-trace-id）
    private String requestId;
    private String traceId;

    public static <T> ApiResponse<T> ok(T data, String requestId, String traceId) {
        ApiResponse<T> r = new ApiResponse<>();
        r.code = 0;
        r.message = "ok";
        r.data = data;
        r.requestId = requestId;
        r.traceId = traceId;
        return r;
    }

    public static <T> ApiResponse<T> fail(int code, String message, String requestId, String traceId) {
        ApiResponse<T> r = new ApiResponse<>();
        r.code = code;
        r.message = message;
        r.data = null;
        r.requestId = requestId;
        r.traceId = traceId;
        return r;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public String getRequestId() { return requestId; }
    public String getTraceId() { return traceId; }

    public void setCode(int code) { this.code = code; }
    public void setMessage(String message) { this.message = message; }
    public void setData(T data) { this.data = data; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
}