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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public static final Logger log = LoggerFactory.getLogger(OrderService.class);

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
    public void processFulfillment(OrderDto orderDto) {
        decrementStock(orderDto);
        sendConfirmationEmail(orderDto);
        handlePacking(orderDto);
        assignDriver(orderDto);
        handleDelivery(orderDto);
    }

    public void decrementStock(OrderDto orderDto){
        log.info("decrease the stock of the products of the order : ", orderDto.getId());
    }

    public void sendConfirmationEmail(OrderDto orderDto){
        log.info("sending confirmation email to the user, confirming the order : ", orderDto.getId());
    }

    public void handlePacking(OrderDto orderDto){
        log.info("handle the packaging of the products of order : ", orderDto.getId());
    }

    public void assignDriver(OrderDto orderDto){
        log.info("assigning a driver for the order : ", orderDto.getId());
    }

    public void handleDelivery(OrderDto orderDto){
        log.info("assigning a truck for the order : ", orderDto.getId());
    }

    @Override
    public List<OrderDto> fetchAllOrders() {

        List<Order> orders = ordersRepository.findAll();
        return orders.stream().map(OrderMapper.MAPPER::maptoOrderDto).toList();

    }
}
