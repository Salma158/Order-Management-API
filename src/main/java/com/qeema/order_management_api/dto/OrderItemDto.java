package com.qeema.order_management_api.dto;

import lombok.Data;

@Data
public class OrderItemDto {

    private Long id;

    private Long productId;

    private Long quantity;
}
