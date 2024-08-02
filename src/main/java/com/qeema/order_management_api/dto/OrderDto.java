package com.qeema.order_management_api.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {

    private Long id;

    private String status;

    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemDto> orderItems;
}
