package com.qeema.order_management_api.service;

import com.qeema.order_management_api.dto.OrderDto;
import com.qeema.order_management_api.dto.OrderItemDto;
import com.qeema.order_management_api.entity.Order;
import com.qeema.order_management_api.entity.OrderItem;
import com.qeema.order_management_api.entity.Product;
import com.qeema.order_management_api.exception.DuplicateProductException;
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

    private OrderDto requestOrderDto;
    private OrderDto expectedOrderDto;
    private Order savedOrder;
    private Product product;

    @BeforeEach
    public void setUp() {

        requestOrderDto = createOrderDto(null, 1L, 2L);

        savedOrder = createOrder(1L, "Pending", 1L, 2L);

        expectedOrderDto = createOrderDto(1L, 1L, 2L);
        expectedOrderDto.setId(1L);
        expectedOrderDto.setStatus("Pending");

        product = new Product();
        product.setId(1L);
        product.setStock(10);
    }

    private OrderDto createOrderDto(Long itemId, Long productId, Long quantity) {
        OrderDto orderDto = new OrderDto();
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(itemId);
        orderItemDto.setProductId(productId);
        orderItemDto.setQuantity(quantity);
        orderDto.setOrderItems(List.of(orderItemDto));
        return orderDto;
    }

    private Order createOrder(Long orderId, String status, Long productId, Long quantity) {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(status);

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setProductId(productId);
        orderItem.setQuantity(quantity);

        order.setOrderItems(List.of(orderItem));
        return order;
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
        assertEquals(expectedOrderDto.getOrderItems().size(), createdOrder.getOrderItems().size());
        assertEquals(expectedOrderDto.getOrderItems().get(0).getId(), createdOrder.getOrderItems().get(0).getId());
        assertEquals(expectedOrderDto.getOrderItems().get(0).getProductId(), createdOrder.getOrderItems().get(0).getProductId());
        assertEquals(expectedOrderDto.getOrderItems().get(0).getQuantity(), createdOrder.getOrderItems().get(0).getQuantity());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Throw DuplicateProductException when duplicate product IDs are provided")
    void testCreateOrder_whenDuplicateProductIds_throwsDuplicateProductException() {
        // Arrange
        OrderDto orderDtoWithDuplicates = createOrderDto(null, 1L, 2L);
        OrderItemDto duplicateItem = new OrderItemDto();
        duplicateItem.setProductId(1L);
        duplicateItem.setQuantity(1L);
        orderDtoWithDuplicates.setOrderItems(List.of(orderDtoWithDuplicates.getOrderItems().get(0), duplicateItem));

        when(productsRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act & Assert
        assertThrows(DuplicateProductException.class, () -> orderService.createOrder(orderDtoWithDuplicates));
    }

    @Test
    @DisplayName("Throw ResourceNotFoundException when product does not exist")
    void testCreateOrder_whenProductDoesNotExist_throwsResourceNotFoundException() {
        // Arrange
        OrderDto invalidOrderDto = createOrderDto(null, 999L, 2L);

        when(productsRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(invalidOrderDto));
    }
}
