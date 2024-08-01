package com.qeema.order_management_api.service.impl;

import com.qeema.order_management_api.dto.ProductDto;
import com.qeema.order_management_api.entity.Product;
import com.qeema.order_management_api.mapper.ProductMapper;
import com.qeema.order_management_api.repository.ProductsRepository;
import com.qeema.order_management_api.service.IProductsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductsService implements IProductsService {

    private ProductsRepository productsRepository;

    @Override
    public ProductDto createProduct(ProductDto productDto) {

        Product product = ProductMapper.MAPPER.maptoProduct(productDto);
        Product savedProduct = productsRepository.save(product);
        ProductDto savedProductDto = ProductMapper.MAPPER.maptoProductDto(savedProduct);
        return savedProductDto;
    }
}
