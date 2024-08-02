package com.qeema.order_management_api.service.impl;

import com.qeema.order_management_api.dto.OrderDto;
import com.qeema.order_management_api.entity.Order;
import com.qeema.order_management_api.entity.OrderItem;
import com.qeema.order_management_api.entity.Product;
import com.qeema.order_management_api.exception.DuplicateProductException;
import com.qeema.order_management_api.exception.InsufficientStockException;
import com.qeema.order_management_api.exception.ResourceNotFoundException;
import com.qeema.order_management_api.mapper.OrderMapper;
import com.qeema.order_management_api.repository.OrdersRepository;
import com.qeema.order_management_api.repository.ProductsRepository;
import com.qeema.order_management_api.service.IOrderService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class OrderService implements IOrderService {

    private OrdersRepository ordersRepository;
    private ProductsRepository productsRepository;

    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {

        Set<Long> productsIds = new HashSet<>();

        orderDto.getOrderItems().forEach(item -> {
            Product product = productsRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "product id", item.getProductId()));

            if (!productsIds.add(item.getProductId())) {
                throw new DuplicateProductException("Duplicate product id: " + item.getProductId());
            }

            if (item.getQuantity() > product.getStock()) {
                throw new InsufficientStockException("Insufficient stock for product id: " + item.getProductId());
            }
        });

        Order order = OrderMapper.MAPPER.maptoOrder(orderDto);
        for (OrderItem item : order.getOrderItems()) {
            item.setOrder(order);
        }

        Order savedOrder = ordersRepository.save(order);
        processFulfillment(savedOrder);
        return OrderMapper.MAPPER.maptoOrderDto(savedOrder);
    }

    @Async
    public void processFulfillment(Order order) {
        decrementStock(order);
        sendConfirmationEmail(order);
        handlePacking(order);
        assignDriver(order);
        handleDelivery(order);
    }

    public void decrementStock(Order order){
        System.out.println("decrease the stock of the product");
    }
    public void sendConfirmationEmail(Order order){
        System.out.println("sending confirmation email to the user");
    }
    public void handlePacking(Order order){
        System.out.println("handle the packaging of the order products");
    }
    public void assignDriver(Order order){
        System.out.println("assigning a driver for the order");
    }
    public void handleDelivery(Order order){
        System.out.println("assigning a truck for the order");
    }


    @Override
    public List<OrderDto> fetchAllOrders() {

        List<Order> orders = ordersRepository.findAll();
        return orders.stream().map(OrderMapper.MAPPER::maptoOrderDto).toList();

    }
}
