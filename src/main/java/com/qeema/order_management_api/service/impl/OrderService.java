package com.qeema.order_management_api.service.impl;

import com.qeema.order_management_api.dto.OrderDto;
import com.qeema.order_management_api.entity.Order;
import com.qeema.order_management_api.mapper.OrderMapper;
import com.qeema.order_management_api.repository.OrdersRepository;
import com.qeema.order_management_api.service.IOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService implements IOrderService {

    private OrdersRepository ordersRepository;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = OrderMapper.MAPPER.maptoOrder(orderDto);
        Order savedOrder = ordersRepository.save(order);
        OrderDto savedOrderDto = OrderMapper.MAPPER.maptoOrderDto(savedOrder);
        return savedOrderDto;
    }
}
