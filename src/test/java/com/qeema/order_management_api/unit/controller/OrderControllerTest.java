package com.qeema.order_management_api.unit.controller;

import com.qeema.order_management_api.controller.OrdersController;
import com.qeema.order_management_api.dto.OrderDto;
import com.qeema.order_management_api.dto.OrderItemDto;
import com.qeema.order_management_api.service.impl.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrdersController ordersController;

    private OrderDto requestOrderDto;
    private OrderDto expectedOrderDto;
    private List<OrderDto> expectedOrderList;

    @BeforeEach
    public void setUp() {

        requestOrderDto = OrderDto.builder()
                .orderItems(List.of(OrderItemDto.builder()
                        .productId(1L)
                        .quantity(2L)
                        .build()))
                .build();

        expectedOrderDto = OrderDto.builder()
                .id(1L)
                .status("Pending")
                .orderItems(List.of(OrderItemDto.builder()
                        .id(1L)
                        .productId(1L)
                        .quantity(2L)
                        .build()))
                .build();

        expectedOrderList = List.of(expectedOrderDto);
    }

    @Test
    @DisplayName("Order can be created")
    void testCreateOrder_whenValidOrderDetailsProvided_returnsCreatedOrderDetails() {
        // Arrange
        when(orderService.createOrder(any(OrderDto.class))).thenReturn(expectedOrderDto);
        doNothing().when(orderService).processFulfillment(any(OrderDto.class));

        // Act
        ResponseEntity<OrderDto> responseEntity = ordersController.createOrder(requestOrderDto);
        OrderDto createdOrder = responseEntity.getBody();

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedOrderDto.getId(), createdOrder.getId());
        assertEquals(expectedOrderDto.getStatus(), createdOrder.getStatus());
        assertEquals(expectedOrderDto.getOrderItems(), createdOrder.getOrderItems());
        verify(orderService).createOrder(any(OrderDto.class));
        verify(orderService).processFulfillment(any(OrderDto.class));
    }

    @Test
    @DisplayName("All created orders are fetched")
    void testFetchAllOrders_returnsListOfOrders() {
        // Arrange
        when(orderService.fetchAllOrders()).thenReturn(expectedOrderList);

        // Act
        ResponseEntity<List<OrderDto>> responseEntity = ordersController.fetchAllOrders();
        List<OrderDto> orders = responseEntity.getBody();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedOrderList, orders);
        verify(orderService).fetchAllOrders();
    }
}
