package com.predators.dto.product;

import lombok.*;

import java.math.BigDecimal;

@Builder
public record ProductRequestDto(String name,
                                String description,
                                BigDecimal price,
                                Long categoryId,
                                String image
                                ) {
}
