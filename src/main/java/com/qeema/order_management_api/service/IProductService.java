package com.qeema.order_management_api.service;

import com.qeema.order_management_api.dto.ProductDto;

public interface IProductService {

    ProductDto createProduct(ProductDto productDto);
}
