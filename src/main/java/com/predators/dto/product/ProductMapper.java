package com.predators.dto.product;

import com.predators.entity.Category;
import com.predators.entity.Product;
import com.predators.service.CategoryService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    @Autowired
    protected CategoryService categoryService;

    @Mapping(source = "categoryId", target = "category", qualifiedByName = "mapCategoryIdToCategory")
    public abstract Product toEntity(ProductRequestDto productDto);

    @Mapping(source = "category.id", target = "categoryId")
    public abstract ProductResponseDto toDto(Product product);

    @Named("mapCategoryIdToCategory")
    protected Category mapCategoryIdToCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryService.getById(categoryId);
    }
}

