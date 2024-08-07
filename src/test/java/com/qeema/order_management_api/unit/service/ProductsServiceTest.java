package com.qeema.order_management_api.unit.service;

import com.qeema.order_management_api.dto.ProductDto;
import com.qeema.order_management_api.entity.Product;
import com.qeema.order_management_api.repository.ProductsRepository;
import com.qeema.order_management_api.service.impl.ProductsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductsServiceTest {

    @Mock
    private ProductsRepository productsRepository;

    @InjectMocks
    private ProductsService productsService;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    public void setUp() {
        product = Product.builder()
                .id(1L)
                .name("watch")
                .price( new BigDecimal(100.0))
                .stock(10)
                .build();

        productDto = ProductDto.builder()
                .id(1L)
                .name("watch")
                .price( new BigDecimal(100.0))
                .stock(10)
                .build();
    }

    @Test
    @DisplayName("Create product with valid details")
    void testCreateProduct_whenValidDetailsProvided_returnsCreatedProduct() {
        // Arrange
        when(productsRepository.save(any(Product.class))).thenReturn(product);

        // Act
        ProductDto createdProductDto = productsService.createProduct(productDto);

        // Assert
        assertEquals(productDto.getId(), createdProductDto.getId());
        assertEquals(productDto.getName(), createdProductDto.getName());
        assertEquals(productDto.getPrice(), createdProductDto.getPrice());
        assertEquals(productDto.getStock(), createdProductDto.getStock());
    }
}
