package com.predators.dto.favorite;

import com.predators.entity.Favorite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class FavoriteMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "product.id", target = "productId")
    public abstract FavoriteResponseDto toDto(Favorite favorite);
}
