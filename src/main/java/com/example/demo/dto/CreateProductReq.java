package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ClassName: CreateProductReq
 * Package: com.example.demo.dto
 * Description:
 * 新增dto
 * @Author 王川
 * @Create 2026/5/17 21:59
 * @Version 1.0
 */
@Data
public class CreateProductReq {

    @NotBlank(message = "name不能为空")
    private String name;
}
