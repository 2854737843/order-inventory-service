package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * ClassName: CreateProductDBReq
 * Package: com.example.demo.dto
 * Description:
 *
 * @Author 王川
 * @Create 2026/5/17 23:45
 * @Version 1.0
 */
@Data
public class CreateProductDBReq {

    @NotBlank(message = "name不能为空")
    private String name;

    @Min(value = 0, message = "stock不能小于0")
    private Integer stock;
}
