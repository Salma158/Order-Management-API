package com.qeema.order_management_api.controller;

import com.qeema.order_management_api.dto.OrderDto;
import com.qeema.order_management_api.dto.OrderItemDto;
import com.qeema.order_management_api.service.impl.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OrderControllerWebLayerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrdersController ordersController;

    private OrderDto orderDto;

    @BeforeEach
    public void setUp() {
        orderDto = new OrderDto();
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(1L);
        orderItemDto.setQuantity(20L);

        List<OrderItemDto> orderItems = List.of(orderItemDto);
        orderDto = new OrderDto();
        orderDto.setOrderItems(orderItems);
    }

    @Test
    @DisplayName("Order can be created")
    void testCreateOrder_whenValidOrderDetailsProvided_returnsCreatedOrderDetails() {

        //Arrange
        when(orderService.createOrder(any(OrderDto.class))).thenReturn(orderDto);

        // Act
        ResponseEntity<OrderDto> responseEntity = ordersController.createOrder(orderDto);
        OrderDto createdOrder = responseEntity.getBody();

        //Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(orderDto, createdOrder);
        verify(orderService).createOrder(any(OrderDto.class));
        verify(orderService).processFulfillment();
}
}