# order-inventory-service

Java 17 + Spring Boot 3 的订单/库存服务练习项目，包含：
- 健康检查 `/health`
- Swagger OpenAPI 文档
- 统一返回体 `Result`
- 全局异常处理（参数校验/业务异常/系统异常）
- traceId（响应头 `X-Trace-Id` + 日志 MDC）

## Tech Stack
- Java 17
- Spring Boot 3
- Maven
- springdoc-openapi (Swagger UI)
- Lombok

## Run Locally

### Prerequisites
- JDK 17
- Maven 3.8+

### Start
在项目根目录执行：

```bash
mvn spring-boot:run
```

或在 IntelliJ IDEA 直接运行启动类（`*Application`）。

## Verify

### Health
- `GET http://localhost:8080/health`
- Response: `ok`

### Swagger UI
- `http://localhost:8080/swagger-ui/index.html`

### OpenAPI JSON
- `http://localhost:8080/v3/api-docs`

### Demo API (Validation + Unified Result)
- `POST http://localhost:8080/api/demo/product`
- Body:
```json
{"name":"apple"}
```

如果 `name` 为空，会返回统一错误结构（参数校验失败）。