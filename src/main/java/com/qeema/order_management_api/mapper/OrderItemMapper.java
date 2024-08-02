package com.qeema.order_management_api.mapper;

import com.qeema.order_management_api.dto.OrderItemDto;
import com.qeema.order_management_api.entity.Order;
import com.qeema.order_management_api.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface OrderItemMapper {

    OrderItemMapper MAPPER = Mappers.getMapper(OrderItemMapper.class);

    OrderItemDto maptoOrderItemDto(OrderItem orderItem);

    OrderItem maptoOrderItem(OrderItemDto orderItemDto);  // Correct method name

}
