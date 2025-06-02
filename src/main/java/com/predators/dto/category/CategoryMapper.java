package com.predators.dto.category;

import com.predators.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {

    public abstract Category toEntity(CategoryRequestDto categoryRequestDto);

    public abstract CategoryResponseDto toDto(Category category);
}
