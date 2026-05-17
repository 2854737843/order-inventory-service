package com.example.demo;

/**
 * ClassName: HealthController
 * Package: com.example.demo
 * Description:
 *
 * @Author 王川
 * @Create 2026/5/17 21:18
 * @Version 1.0
 */
import com.example.demo.common.api.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/health")
    public String health() {
        return "ok";
    }

    // 随便找个 controller 类里加
    @GetMapping("/api/test")
    public Result<String> test() {
        return Result.ok("hello");
    }
}