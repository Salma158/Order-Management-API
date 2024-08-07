package com.qeema.order_management_api.integration;

import com.qeema.order_management_api.constants.OrderStatus;
import com.qeema.order_management_api.dto.OrderDto;
import com.qeema.order_management_api.entity.Order;
import com.qeema.order_management_api.entity.OrderItem;
import com.qeema.order_management_api.entity.Product;
import com.qeema.order_management_api.repository.OrdersRepository;
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
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrdersControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private OrdersRepository orderRepository;

    private HttpHeaders headers;

    private List<Product> savedProducts;

    @BeforeEach
    void setup() {
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

        savedProducts = Arrays.asList(product1, product2);
        productsRepository.saveAll(savedProducts);

        OrderItem orderItem = OrderItem.builder()
                .productId(product2.getId())
                .quantity(2L)
                .build();

        Order order1 = Order.builder()
                .orderItems(List.of(orderItem))
                .status(OrderStatus.PENDING)
                .build();

        orderItem.setOrder(order1);
        orderRepository.save(order1);
    }

    @AfterEach
    void cleanup() {
        orderRepository.deleteAll();
        productsRepository.deleteAll();
    }


    @Test
    @DisplayName("create order with valid order details")
    void testCreateOrder_whenValidDetailsProvided_returnsOrderDetails() throws JSONException {
        // Arrange
        JSONObject orderDetailsRequestJson = createOrderDetailsRequestJson(savedProducts);
        HttpEntity<String> request = new HttpEntity<>(orderDetailsRequestJson.toString(), headers);

        // Act
        ResponseEntity<OrderDto> createdOrderResponse = testRestTemplate.postForEntity("/api/orders", request, OrderDto.class);
        OrderDto createdOrderDetails = createdOrderResponse.getBody();

        // Assert
        assertEquals(HttpStatus.CREATED, createdOrderResponse.getStatusCode());
        assertNotNull(createdOrderDetails);
        assertNotNull(createdOrderDetails.getId());
        assertEquals(2, createdOrderDetails.getOrderItems().size());
    }

    @Test
    @DisplayName("Attempt to create order with non-existent product")
    void testCreateOrder_withNonExistentProduct_returnsNotFound() throws JSONException {
        // Arrange
        JSONObject orderDetailsRequestJson = new JSONObject();
        JSONArray orderItemsArray = new JSONArray();

        JSONObject orderItem1 = new JSONObject();
        orderItem1.put("productId", 999L); // non-existent product ID
        orderItem1.put("quantity", 1);
        orderItemsArray.put(orderItem1);

        orderDetailsRequestJson.put("orderItems", orderItemsArray);
        HttpEntity<String> request = new HttpEntity<>(orderDetailsRequestJson.toString(), headers);

        // Act
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity("/api/orders", request, String.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Attempt to create order with quantity exceeding stock")
    void testCreateOrder_withQuantityExceedingStock_returnsBadRequest() throws JSONException {
        // Arrange
        JSONObject orderDetailsRequestJson = new JSONObject();
        JSONArray orderItemsArray = new JSONArray();

        JSONObject orderItem1 = new JSONObject();
        orderItem1.put("productId", savedProducts.get(0).getId()); // valid product ID
        orderItem1.put("quantity", 20); // quantity exceeding stock
        orderItemsArray.put(orderItem1);

        orderDetailsRequestJson.put("orderItems", orderItemsArray);
        HttpEntity<String> request = new HttpEntity<>(orderDetailsRequestJson.toString(), headers);

        // Act
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity("/api/orders", request, String.class);

        // Assert
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Attempt to create order with duplicate products")
    void testCreateOrder_withDuplicateProducts_returnsBadRequest() throws JSONException {
        // Arrange
        JSONObject orderDetailsRequestJson = new JSONObject();
        JSONArray orderItemsArray = new JSONArray();

        JSONObject orderItem1 = new JSONObject();
        orderItem1.put("productId", savedProducts.get(0).getId()); // valid product ID
        orderItem1.put("quantity", 2);
        orderItemsArray.put(orderItem1);

        JSONObject orderItem2 = new JSONObject();
        orderItem2.put("productId", savedProducts.get(0).getId()); // same product ID
        orderItem2.put("quantity", 1);
        orderItemsArray.put(orderItem2);

        orderDetailsRequestJson.put("orderItems", orderItemsArray);
        HttpEntity<String> request = new HttpEntity<>(orderDetailsRequestJson.toString(), headers);

        // Act
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity("/api/orders", request, String.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    private JSONObject createOrderDetailsRequestJson(List<Product> products) throws JSONException {
        JSONObject orderDetailsRequestJson = new JSONObject();
        JSONArray orderItemsArray = new JSONArray();

        JSONObject orderItem1 = new JSONObject();
        orderItem1.put("productId", products.get(0).getId());
        orderItem1.put("quantity", 2);
        orderItemsArray.put(orderItem1);

        JSONObject orderItem2 = new JSONObject();
        orderItem2.put("productId", products.get(1).getId());
        orderItem2.put("quantity", 1);
        orderItemsArray.put(orderItem2);

        orderDetailsRequestJson.put("orderItems", orderItemsArray);
        return orderDetailsRequestJson;
    }

    @Test
    @DisplayName("Fetch all orders")
    void testFetchAllOrders_returnsOrderList() {
        // Act
        ResponseEntity<OrderDto[]> response = testRestTemplate.getForEntity("/api/orders", OrderDto[].class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        OrderDto[] orders = response.getBody();
        assertEquals(1, orders.length);
    }
}