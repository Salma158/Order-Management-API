package com.qeema.order_management_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor // Add this
@AllArgsConstructor
public class OrderDto {

    private Long id;

    private String status;

    @NotEmpty(message = "Order items cannot be empty")
    @Valid
    private List<OrderItemDto> orderItems;
}
