package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderReq {

    @NotNull(message = "productId不能为空")
    private Long productId;

    @NotNull(message = "count不能为空")
    @Min(value = 1, message = "count必须>=1")
    private Integer count;

}