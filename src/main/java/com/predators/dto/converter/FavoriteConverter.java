package com.predators.dto.converter;

import com.predators.dto.favorite.FavoriteRequestDto;
import com.predators.dto.favorite.FavoriteResponseDto;
import com.predators.entity.Favorite;
import org.springframework.stereotype.Component;

@Component
public class FavoriteConverter implements Converter<FavoriteRequestDto, FavoriteResponseDto, Favorite> {
    @Override
    public FavoriteResponseDto toDto(Favorite favorite) {
        return new FavoriteResponseDto(favorite.getId(), favorite.getUser().getId(), favorite.getProduct().getId());
    }

    @Override
    public Favorite toEntity(FavoriteRequestDto favoriteRequestDto) {
        return null;
    }
}
