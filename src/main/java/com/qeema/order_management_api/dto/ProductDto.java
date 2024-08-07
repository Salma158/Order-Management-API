package com.qeema.order_management_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Product",
        description = "Schema to hold the Product details"
)
public class ProductDto {

    @Schema(
            description = "Id of the Product, Auto incremented"
    )
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Schema(
            description = "Name of the Product",
            example = "Face Serum"
    )
    private String name;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be greater than zero")
    @Schema(
            description = "Price of the Product",
            example = "20.39",
            format = "decimal"
    )
    private BigDecimal price;

    @NotNull(message = "Stock cannot be null")
    @Positive(message = "Stock must be greater than or equal to zero")
    @Schema(
            description = "Number of stock items of the Product",
            example = "120"
    )
    private Integer stock;
}
