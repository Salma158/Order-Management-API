package com.qeema.order_management_api.dto;

import com.qeema.order_management_api.constants.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Order",
        description = "Schema to hold the Order details"
)
public class OrderDto {

    @Schema(
            description = "Id of the Order, Auto incremented"
    )
    private Long id;

    @Schema(description = "Status of the order",
            example = "SHIPPED")
    private OrderStatus status;

    @NotEmpty(message = "Order items cannot be empty")
    @Valid
    @Schema(
            description = "List of the order items details"
    )
    private List<OrderItemDto> orderItems;
}
