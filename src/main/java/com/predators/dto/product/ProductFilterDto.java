package com.predators.dto.product;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductFilterDto(BigDecimal minPrice,
                               BigDecimal maxPrice,
                               Boolean discountPrice,
                               Long categoryId) {
}
