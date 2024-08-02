package com.qeema.order_management_api.mapper;

import com.qeema.order_management_api.dto.OrderDto;
import com.qeema.order_management_api.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = OrderItemMapper.class)
public interface OrderMapper {

    OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "orderItems", source = "orderItems")
    OrderDto maptoOrderDto(Order order);

    @Mapping(target = "orderItems", source = "orderItems")
    Order maptoOrder(OrderDto orderDto);
}
