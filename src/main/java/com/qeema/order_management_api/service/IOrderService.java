package com.qeema.order_management_api.service;

import com.qeema.order_management_api.dto.OrderDto;

public interface IOrderService {

    OrderDto createOrder(OrderDto orderDto);
}
