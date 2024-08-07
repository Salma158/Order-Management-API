package com.qeema.order_management_api.unit.service;

import com.qeema.order_management_api.constants.OrderStatus;
import com.qeema.order_management_api.dto.OrderDto;
import com.qeema.order_management_api.dto.OrderItemDto;
import com.qeema.order_management_api.entity.Order;
import com.qeema.order_management_api.entity.OrderItem;
import com.qeema.order_management_api.entity.Product;
import com.qeema.order_management_api.exception.DuplicateProductException;
import com.qeema.order_management_api.exception.InsufficientStockException;
import com.qeema.order_management_api.exception.ResourceNotFoundException;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrdersRepository orderRepository;

    @Mock
    private ProductsRepository productsRepository;

    @InjectMocks
    private OrderService orderService;

    private Product product;

    @BeforeEach
    public void setUp() {

        product = Product.builder()
                .id(1L)
                .stock(10)
                .build();
    }

    @Test
    @DisplayName("Create order with valid details")
    void testCreateOrder_whenValidOrderDetailsProvided_returnsCreatedOrder() {
        // Arrange
        OrderDto requestOrderDto = OrderDto.builder()
                .orderItems(List.of(OrderItemDto.builder()
                        .productId(1L)
                        .quantity(2L)
                        .build()))
                .build();

        Order savedOrder = Order.builder()
                .id(1L)
                .status(OrderStatus.PENDING)
                .orderItems(List.of(OrderItem.builder()
                        .id(1L)
                        .productId(1L)
                        .quantity(2L)
                        .build()))
                .build();

        OrderDto expectedOrderDto = OrderDto.builder()
                .id(1L)
                .status(OrderStatus.PENDING)
                .orderItems(List.of(OrderItemDto.builder()
                        .id(1L)
                        .productId(1L)
                        .quantity(2L)
                        .build()))
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(productsRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        OrderDto createdOrder = orderService.createOrder(requestOrderDto);

        // Assert
        assertEquals(expectedOrderDto.getId(), createdOrder.getId());
        assertEquals(expectedOrderDto.getStatus(), createdOrder.getStatus());
        assertEquals(expectedOrderDto.getOrderItems().size(), createdOrder.getOrderItems().size());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Throw DuplicateProductException when duplicate product IDs are provided")
    void testCreateOrder_whenDuplicateProductIds_throwsDuplicateProductException() {
        // Arrange
        OrderDto orderDtoWithDuplicates = OrderDto.builder()
                .orderItems(List.of(
                        OrderItemDto.builder()
                                .productId(1L)
                                .quantity(2L)
                                .build(),
                        OrderItemDto.builder()
                                .productId(1L)
                                .quantity(1L)
                                .build()))
                .build();

        when(productsRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act & Assert
        assertThrows(DuplicateProductException.class, () -> orderService.createOrder(orderDtoWithDuplicates));
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when product does not exist")
    void testCreateOrder_whenProductDoesNotExist_throwsResourceNotFoundException() {
        // Arrange
        OrderDto invalidOrderDto = OrderDto.builder()
                .orderItems(List.of(OrderItemDto.builder()
                        .productId(3L)
                        .quantity(2L)
                        .build()))
                .build();

        when(productsRepository.findById(3L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(invalidOrderDto));
    }

    @Test
    @DisplayName("Throw InsufficientStockException when quantity exceeds stock")
    void testCreateOrder_whenQuantityExceedsStock_throwsInsufficientStockException() {
        // Arrange
        OrderDto orderDtoWithInsufficientStock = OrderDto.builder()
                .orderItems(List.of(OrderItemDto.builder()
                        .productId(1L)
                        .quantity(20L)
                        .build()))
                .build();

        when(productsRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act & Assert
        assertThrows(InsufficientStockException.class, () -> orderService.createOrder(orderDtoWithInsufficientStock));
    }

    @Test
    @DisplayName("Fetch all orders")
    void testFetchAllOrders() {
        // Arrange
        Order expectedOrder = Order.builder()
                .id(1L)
                .status(OrderStatus.PENDING)
                .orderItems(List.of(OrderItem.builder()
                        .id(1L)
                        .productId(1L)
                        .quantity(2L)
                        .build()))
                .build();

        OrderDto expectedOrderDto = OrderDto.builder()
                .id(1L)
                .status(OrderStatus.PENDING)
                .orderItems(List.of(OrderItemDto.builder()
                        .id(1L)
                        .productId(1L)
                        .quantity(2L)
                        .build()))
                .build();

        when(orderRepository.findAll()).thenReturn(List.of(expectedOrder));

        // Act
        List<OrderDto> actualOrderDtos = orderService.fetchAllOrders();

        // Assert
        assertEquals(List.of(expectedOrderDto), actualOrderDtos);
    }
}
