package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * ClassName: DeductStockReq
 * Package: com.example.demo.dto
 * Description:
 *
 * @Author 王川
 * @Create 2026/5/18 15:01
 * @Version 1.0
 */
@Data
public class DeductStockReq {

    @Min(value = 1, message = "count必须>=1")
    private Integer count;
}