package com.predators.dto.favorite;

import lombok.Builder;

@Builder
public record FavoriteResponseDto(Long id,
                                  Long userId,
                                  Long productId) {
}
