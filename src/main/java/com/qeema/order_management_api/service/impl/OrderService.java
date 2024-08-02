package com.qeema.order_management_api.service.impl;

import com.qeema.order_management_api.dto.OrderDto;
import com.qeema.order_management_api.entity.Order;
import com.qeema.order_management_api.entity.OrderItem;
import com.qeema.order_management_api.mapper.OrderMapper;
import com.qeema.order_management_api.repository.OrdersItemsRepository;
import com.qeema.order_management_api.repository.OrdersRepository;
import com.qeema.order_management_api.service.IOrderService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService implements IOrderService {

    private OrdersRepository ordersRepository;

    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {

        Order order = OrderMapper.MAPPER.maptoOrder(orderDto);
        for (OrderItem item : order.getOrderItems()) {
            item.setOrder(order);
        }
        order = ordersRepository.save(order);
        OrderDto savedOrderDto = OrderMapper.MAPPER.maptoOrderDto(order);
        return savedOrderDto;
    }
}
