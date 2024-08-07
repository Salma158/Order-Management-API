package com.qeema.order_management_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Order Item",
        description = "Schema to hold the Order item details"
)
public class OrderItemDto {

    @Schema(
            description = "Id of the Order Item, Auto incremented"
    )
    private Long id;

    @NotNull(message = "Product ID cannot be null")
    @Schema(
            description = "Id of the product, Auto incremented",
            example = "7"
    )
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be greater than zero")
    @Schema(
            description = "Quantity of the product",
            example = "200"
    )
    private Long quantity;
}
