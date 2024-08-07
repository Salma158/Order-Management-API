package com.qeema.order_management_api.controller;

import com.qeema.order_management_api.dto.ErrorResponseDto;
import com.qeema.order_management_api.dto.OrderDto;
import com.qeema.order_management_api.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/orders", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@Tag(
        name = "Create, Read REST APIs for the Orders",
        description = "REST APIs to CREATE AND FETCH Orders details"
)
public class OrdersController {

    private IOrderService IOrderService;

    @Operation(
            summary = "Create Order REST API",
            description = "REST API to Create Order"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody @Valid OrderDto orderDto){
        OrderDto createdOrder = IOrderService.createOrder(orderDto);
        IOrderService.processFulfillment(createdOrder);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdOrder);
    }

    @Operation(
            summary = "Fetch Alls Order REST API",
            description = "REST API to fetch All Created Orders"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping
    public ResponseEntity<List<OrderDto>> fetchAllOrders(){
        List<OrderDto> orders = IOrderService.fetchAllOrders();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orders);
    }
}
