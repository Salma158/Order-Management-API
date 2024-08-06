package com.qeema.order_management_api.controller;

import com.qeema.order_management_api.dto.ProductDto;
import com.qeema.order_management_api.repository.ProductsRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductsControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ProductsRepository productsRepository;

    private HttpHeaders headers;

    @BeforeEach
    void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @AfterEach
    void cleanup() {
        productsRepository.deleteAll();
    }

    @Test
    @DisplayName("Product can be created")
    void testCreateProduct_whenValidDetailsProvided_returnsProductDetails() throws JSONException {
        // Arrange
        JSONObject productDetailsRequestJson = new JSONObject();
        productDetailsRequestJson.put("name", "Iphone");
        productDetailsRequestJson.put("price", 50000.22);
        productDetailsRequestJson.put("stock", 10);
        HttpEntity<String> request = new HttpEntity<>(productDetailsRequestJson.toString(), headers);

        // Act
        ResponseEntity<ProductDto> createdProductResponse = testRestTemplate.postForEntity("/api/products", request, ProductDto.class);
        ProductDto createdProductDetails = createdProductResponse.getBody();

        // Assert
        assertEquals(HttpStatus.CREATED, createdProductResponse.getStatusCode());
        assertNotNull(createdProductDetails);
        assertNotNull(createdProductDetails.getId());
        assertEquals("Iphone", createdProductDetails.getName());
        assertEquals(new BigDecimal("50000.22"), createdProductDetails.getPrice());
        assertEquals(10, createdProductDetails.getStock());
    }


    @Test
    @DisplayName("Attempt to create product with invalid details returns BadRequest")
    void testCreateProduct_withInvalidDetails_returnsBadRequest() throws JSONException {
        // Arrange
        JSONObject productDetailsRequestJson = new JSONObject();
        productDetailsRequestJson.put("name", ""); // Invalid name
        productDetailsRequestJson.put("price", 50000.00);
        productDetailsRequestJson.put("stock", 10);
        HttpEntity<String> request = new HttpEntity<>(productDetailsRequestJson.toString(), headers);

        // Act
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity("/api/products", request, String.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
