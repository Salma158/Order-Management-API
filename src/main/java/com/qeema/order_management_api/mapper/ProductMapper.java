package com.qeema.order_management_api.mapper;

import com.qeema.order_management_api.dto.ProductDto;
import com.qeema.order_management_api.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

    ProductDto maptoProductDto(Product product);

    Product maptoProduct(ProductDto productDto);
}
