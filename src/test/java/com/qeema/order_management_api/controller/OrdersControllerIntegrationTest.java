package com.qeema.order_management_api.controller;

import com.qeema.order_management_api.dto.OrderDto;
import com.qeema.order_management_api.entity.Product;
import com.qeema.order_management_api.repository.ProductsRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrdersControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ProductsRepository productsRepository;

    private HttpHeaders headers;

    @BeforeEach
    void setup() {

        productsRepository.deleteAll();

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        Product product1 = Product.builder()
                .name("Iphone")
                .stock(10)
                .price(BigDecimal.valueOf(50000.00))
                .build();
        Product product2 = Product.builder()
                .name("Headphones")
                .price(BigDecimal.valueOf(500))
                .stock(10)
                .build();

        productsRepository.save(product1);
        productsRepository.save(product2);
    }

    @Test
    @DisplayName("Order can be created")
    void testCreateOrder_whenValidDetailsProvided_returnsOrderDetails() throws JSONException {
        // Arrange
        JSONObject orderDetailsRequestJson = createOrderDetailsRequestJson();

        HttpEntity<String> request = new HttpEntity<>(orderDetailsRequestJson.toString(), headers);

        System.out.println(request);

        // Act
        ResponseEntity<OrderDto> createdOrderResponse = testRestTemplate.postForEntity("/api/orders", request, OrderDto.class);
        System.out.println(createdOrderResponse);
        OrderDto createdOrderDetails = createdOrderResponse.getBody();

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, createdOrderResponse.getStatusCode());
        Assertions.assertNotNull(createdOrderDetails);
        Assertions.assertNotNull(createdOrderDetails.getId());
        Assertions.assertEquals(2, createdOrderDetails.getOrderItems().size());
    }

    private JSONObject createOrderDetailsRequestJson() throws JSONException {
        JSONObject orderDetailsRequestJson = new JSONObject();
        JSONArray orderItemsArray = new JSONArray();

        JSONObject orderItem1 = new JSONObject();
        orderItem1.put("productId", 1);
        orderItem1.put("quantity", 2);
        orderItemsArray.put(orderItem1);

        JSONObject orderItem2 = new JSONObject();
        orderItem2.put("productId", 2);
        orderItem2.put("quantity", 1);
        orderItemsArray.put(orderItem2);

        orderDetailsRequestJson.put("orderItems", orderItemsArray);
        return orderDetailsRequestJson;
    }
}
