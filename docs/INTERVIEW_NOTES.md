# 面试讲稿（1分钟版本）

我做了一个订单服务，重点解决三个工程问题：统一返回、异常治理、幂等与链路排查。

1) **统一返回体**
- 所有 Controller 统一返回 `Result<T>`，包含 `code/message/data`，并在返回阶段自动注入 `requestId/traceId`。
- 好处：前端处理简单、接口规范一致。

2) **全局异常治理**
- 业务异常（BizException）映射为业务错误码
- 参数校验异常统一返回 `PARAM_INVALID`
- 系统异常统一返回 `INTERNAL_ERROR`，对外不暴露堆栈，对内记录 error 日志 + requestId/traceId

3) **幂等**
- 下单接口支持 `X-Request-Id` 幂等：同 requestId + 同参数返回同订单号；requestId 相同但参数不同返回幂等冲突。
- 取消接口支持重复调用（幂等/或至少保证不会二次扣减/回补异常）

4) **链路日志**
- 在 Filter 中生成/透传 requestId、traceId 写入 MDC
- 日志 pattern 统一输出 `[requestId=..., traceId=...]`，出问题能快速定位到一次请求的所有日志

5) **自动化测试**
- 用 SpringBootTest + MockMvc 写了 3 个集成测试：幂等、库存不足、取消幂等，保证关键能力可回归。