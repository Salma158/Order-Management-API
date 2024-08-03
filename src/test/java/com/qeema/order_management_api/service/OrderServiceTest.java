package com.qeema.order_management_api.service;

import com.qeema.order_management_api.dto.OrderDto;
import com.qeema.order_management_api.dto.OrderItemDto;
import com.qeema.order_management_api.entity.Order;
import com.qeema.order_management_api.entity.OrderItem;
import com.qeema.order_management_api.entity.Product;
import com.qeema.order_management_api.repository.OrdersRepository;
import com.qeema.order_management_api.repository.ProductsRepository;
import com.qeema.order_management_api.service.impl.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrdersRepository orderRepository;

    @Mock
    private ProductsRepository productsRepository;

    @InjectMocks
    private OrderService orderService;


    private OrderDto requestOrderDto;
    private OrderDto expectedOrderDto;
    private Order savedOrder;
    private Product product;


    @BeforeEach
    public void setUp() {
        // Initialize request OrderDto
        requestOrderDto = new OrderDto();
        OrderItemDto requestOrderItem = new OrderItemDto();
        requestOrderItem.setProductId(1L);
        requestOrderItem.setQuantity(2L);

        List<OrderItemDto> requestOrderItems = List.of(requestOrderItem);
        requestOrderDto.setOrderItems(requestOrderItems);

        // simulate saved order
        OrderItem savedOrderItem = new OrderItem();
        savedOrderItem.setId(1L);
        savedOrderItem.setProductId(1L);
        savedOrderItem.setQuantity(2L);

        List<OrderItem> savedOrderItems = List.of(savedOrderItem);

        savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setStatus("Pending");
        savedOrder.setOrderItems(savedOrderItems);

        // Initialize expected order
        OrderItemDto excpectedOrderItemDto = new OrderItemDto();
        excpectedOrderItemDto.setId(1L);
        excpectedOrderItemDto.setProductId(1L);
        excpectedOrderItemDto.setQuantity(2L);

        List<OrderItemDto> excpectedOrderItems = List.of(excpectedOrderItemDto);

        expectedOrderDto = new OrderDto();
        expectedOrderDto.setId(1L);
        expectedOrderDto.setStatus("Pending");
        expectedOrderDto.setOrderItems(excpectedOrderItems);

        // Initialize product
        product = new Product();
        product.setId(1L);
        product.setStock(10);

    }

    @Test
    @DisplayName("Create order with valid details")
    void testCreateOrder_whenValidOrderDetailsProvided_returnsCreatedOrder() {
        // Arrange
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(productsRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        OrderDto createdOrder = orderService.createOrder(requestOrderDto);

        // Assert
        assertEquals(expectedOrderDto.getId(), createdOrder.getId());
        assertEquals(expectedOrderDto.getStatus(), createdOrder.getStatus());
        assertEquals(expectedOrderDto.getOrderItems(), createdOrder.getOrderItems());
        verify(orderRepository).save(any(Order.class));

    }
}
