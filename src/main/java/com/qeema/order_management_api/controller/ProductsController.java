package com.qeema.order_management_api.controller;

import com.qeema.order_management_api.dto.ProductDto;
import com.qeema.order_management_api.repository.ProductsRepository;
import com.qeema.order_management_api.service.IProductsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/products")
public class ProductsController {

    private IProductsService productsService;

    @PostMapping
    public ResponseEntity<ProductDto> createMenuItem(@RequestBody ProductDto productDto){
        ProductDto createdProduct = productsService.createProduct(productDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProduct);
    }

}