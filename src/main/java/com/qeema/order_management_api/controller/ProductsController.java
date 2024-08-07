package com.qeema.order_management_api.controller;

import com.qeema.order_management_api.dto.ErrorResponseDto;
import com.qeema.order_management_api.dto.ProductDto;
import com.qeema.order_management_api.service.IProductsService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/products", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@Tag(
        name = "Create REST API for the Products",
        description = "REST API to CREATE Products"
)
public class ProductsController {

    private IProductsService productsService;

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
    public ResponseEntity<ProductDto> createMenuItem(@RequestBody @Valid ProductDto productDto){
        ProductDto createdProduct = productsService.createProduct(productDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProduct);
    }

}
