package com.predators.dto.product;

import lombok.Builder;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Builder
public record ProductResponseDto(Long id,
                                 String name,
                                 String description,
                                 BigDecimal price,
                                 Long categoryId,
                                 String imageUrl,
                                 BigDecimal discountPrice,
                                 Timestamp createdAt,
                                 Timestamp updatedAt) {
}
