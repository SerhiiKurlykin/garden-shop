package com.predators.dto.converter;

import com.predators.dto.product.ProductRequestDto;
import com.predators.dto.product.ProductResponseDto;
import com.predators.entity.Category;
import com.predators.entity.Product;
import com.predators.service.CategoryService;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter implements Converter<ProductRequestDto, ProductResponseDto, Product> {
    private final CategoryService categoryService;

    public ProductConverter(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public Product toEntity(ProductRequestDto productDto) {
        Category category = categoryService.getById(productDto.categoryId());

        return Product.builder()
                .name(productDto.name())
                .description(productDto.description())
                .price(productDto.price())
                .category(category)
                .imageUrl(productDto.image())
                .build();
    }

    public ProductResponseDto toDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .categoryId(product.getCategory() == null ? null : product.getCategory().getId())
                .image(product.getImageUrl())
                .discountPrice(product.getDiscountPrice())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }


}
