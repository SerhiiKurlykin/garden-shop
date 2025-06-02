package com.predators.service;

import com.predators.dto.product.ProductCountDto;
import com.predators.dto.product.ProductFilterDto;
import com.predators.dto.product.ProductRequestDto;
import com.predators.entity.Category;
import com.predators.entity.Product;
import com.predators.entity.enums.OrderStatus;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

public interface ProductService {

    List<Product> getAll();

    Page<Product> getAll(ProductFilterDto filter, int page, int size, String[] sort) throws BadRequestException;

    Product create(Product product);

    Product getById(Long id);

    void delete(Long id);

    Product update(Long id, ProductRequestDto productRequestDto);

    void updateCategory(Long id, Category category);

    List<Product> findByCategoryId(Long categoryId);

    Product setDiscount(Long id, BigDecimal discount);

    Product getDayProduct();

    List<ProductCountDto> findTopProductsAndCountsByOrderStatus(OrderStatus status, int limit);

    Set<Product> findByStatusAndUpdatedAtBeforeThreshold(OrderStatus status, Timestamp data);
}
