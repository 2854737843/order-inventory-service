# demo - 订单幂等 / 统一返回 / 链路日志

## 技术栈
- Java 17
- Spring Boot 3.x
- MyBatis
- MySQL
- JUnit5 + MockMvc（集成测试）

## 已实现功能
- 订单创建（支持幂等）：同一个 `X-Request-Id` + 相同参数，返回同一订单号；参数不一致返回幂等冲突
- 订单取消（取消重复调用可幂等）
- 统一返回体 `Result<T>`：`{code,message,data,requestId,traceId}`
- 全局异常处理：业务异常/参数校验异常/系统异常统一映射
- requestId/traceId 链路：
    - Filter 写入 MDC：日志自动带 `requestId/traceId`
    - 响应体自动注入 requestId/traceId（ResponseBodyAdvice）

## 接口示例
### 创建订单
POST `/api/order`

Header:
- `X-Request-Id: any-string`（可选，不传服务端会生成）

Body:
```json
{"productId":12,"count":1}
```

Response:
```json
{"code":0,"message":"OK","data":63,"requestId":"...","traceId":"..."}
```

### 取消订单
POST `/api/order/{id}/cancel`

## 运行方式
1. 准备 MySQL 并配置 `application.yml` 数据源
2. 启动：
    - IntelliJ IDEA 运行 `DemoApplication`
    - 或 `mvn spring-boot:run`

## 测试
运行：
- `mvn -Dtest=OrderApiTest test`

覆盖点：
- 幂等：同 requestId 重复提交返回同 orderId
- 库存不足错误码校验
- 取消重复调用可用