package com.qeema.order_management_api.service;

import com.qeema.order_management_api.dto.ProductDto;

public interface IProductsService {

    ProductDto createProduct(ProductDto productDto);
}
