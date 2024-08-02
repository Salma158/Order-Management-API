package com.qeema.order_management_api.controller;

import com.qeema.order_management_api.dto.OrderDto;
import com.qeema.order_management_api.service.IOrderService;
import com.qeema.order_management_api.service.IProductsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/orders")
@Validated
public class OrdersController {

    private IOrderService IOrderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid OrderDto orderDto){
        OrderDto createdOrder = IOrderService.createOrder(orderDto);
        IOrderService.processFulfillment();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdOrder);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> fetchAllOrders(){
        List<OrderDto> orders = IOrderService.fetchAllOrders();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orders);
    }
}
