package com.qeema.order_management_api.controller;

import com.qeema.order_management_api.dto.OrderDto;
import com.qeema.order_management_api.dto.OrderItemDto;
import com.qeema.order_management_api.service.impl.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OrderControllerWebLayerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrdersController ordersController;

    @Test
    @DisplayName("Order can be created")
    void testCreateOrder_whenValidOrderDetailsProvided_returnsCreatedOrderDetails() {

        //Arrange
        OrderDto orderDto = new OrderDto();
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(1L);
        orderItemDto.setQuantity(20L);

        List<OrderItemDto> orderItems = List.of(orderItemDto);
        orderDto.setOrderItems(orderItems);

        OrderDto createdOrderDto = new OrderDto();
        createdOrderDto.setOrderItems(orderItems);
        createdOrderDto.setId(1L);
        createdOrderDto.setStatus("Pending");

        when(orderService.createOrder(any(OrderDto.class))).thenReturn(createdOrderDto);

        // Act
        ResponseEntity<OrderDto> responseEntity = ordersController.createOrder(orderDto);
        OrderDto createdOrder = responseEntity.getBody();

        //Assert
        assert createdOrder != null;
        Assertions.assertEquals(orderDto.getOrderItems().size(),
                createdOrder.getOrderItems().size(),
                "Didn't return the correct orderItems list length");
        verify(orderService).createOrder(any(OrderDto.class));
    }
}
