package com.qeema.order_management_api.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderDto {

    private List<OrderItemDto> items;
}