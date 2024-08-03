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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderService implements IOrderService {

    private final OrdersRepository ordersRepository;
    private final ProductsRepository productsRepository;

    public OrderService(OrdersRepository ordersRepository, ProductsRepository productsRepository){
        this.ordersRepository = ordersRepository;
        this.productsRepository = productsRepository;
    }

    @Value("${order.default.status}")
    private String defaultStatus;

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
        order.setStatus(defaultStatus);
        for (OrderItem item : order.getOrderItems()) {
            item.setOrder(order);
        }

        Order savedOrder = ordersRepository.save(order);
        return OrderMapper.MAPPER.maptoOrderDto(savedOrder);
    }

    @Async
    public void processFulfillment() {
        decrementStock();
        sendConfirmationEmail();
        handlePacking();
        assignDriver();
        handleDelivery();
    }

    public void decrementStock(){
            System.out.println("decrease the stock of the product");
    }

    public void sendConfirmationEmail(){
        System.out.println("sending confirmation email to the user");
    }

    public void handlePacking(){
        System.out.println("handle the packaging of the order products");
    }

    public void assignDriver(){
        System.out.println("assigning a driver for the order");
    }

    public void handleDelivery(){
        System.out.println("assigning a truck for the order");
    }

    @Override
    public List<OrderDto> fetchAllOrders() {

        List<Order> orders = ordersRepository.findAll();
        return orders.stream().map(OrderMapper.MAPPER::maptoOrderDto).toList();

    }
}
