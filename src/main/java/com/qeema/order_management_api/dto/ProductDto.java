package com.qeema.order_management_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor // Add this
@AllArgsConstructor
public class ProductDto {
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

    @NotNull(message = "Stock cannot be null")
    @Positive(message = "Stock must be greater than or equal to zero")
    private Integer stock;
}

