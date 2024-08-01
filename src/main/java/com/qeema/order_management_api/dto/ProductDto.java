package com.qeema.order_management_api.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;

    private String name;

    private BigDecimal price;

    private Integer stock;
}
