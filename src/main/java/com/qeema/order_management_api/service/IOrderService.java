package com.qeema.order_management_api.service;

import com.qeema.order_management_api.dto.OrderDto;

import java.util.List;

public interface IOrderService {

    OrderDto createOrder(OrderDto orderDto);

    List<OrderDto> fetchAllOrders();

    void processFulfillment();
}
