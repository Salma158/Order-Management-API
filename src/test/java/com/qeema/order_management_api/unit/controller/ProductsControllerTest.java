package com.qeema.order_management_api.unit.controller;

import com.qeema.order_management_api.controller.ProductsController;
import com.qeema.order_management_api.dto.ProductDto;
import com.qeema.order_management_api.service.IProductsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductsControllerTest {

    @Mock
    private IProductsService productsService;

    @InjectMocks
    private ProductsController productsController;

    private ProductDto requestProductDto;
    private ProductDto expectedProductDto;

    @BeforeEach
    public void setUp() {
        requestProductDto = ProductDto.builder()
                .name("laptop")
                .price( new BigDecimal(100.0))
                .build();

        expectedProductDto = ProductDto.builder()
                .id(1L)
                .name("laptop")
                .price( new BigDecimal(100.0))
                .build();
    }

    @Test
    @DisplayName("Product can be created")
    void testCreateProduct_whenValidProductDetailsProvided_returnsCreatedProductDetails() {
        // Arrange
        when(productsService.createProduct(any(ProductDto.class))).thenReturn(expectedProductDto);

        // Act
        ResponseEntity<ProductDto> responseEntity = productsController.createMenuItem(requestProductDto);
        ProductDto createdProduct = responseEntity.getBody();

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedProductDto.getId(), createdProduct.getId());
        assertEquals(expectedProductDto.getName(), createdProduct.getName());
        assertEquals(expectedProductDto.getPrice(), createdProduct.getPrice());
        verify(productsService).createProduct(any(ProductDto.class));
    }
}
