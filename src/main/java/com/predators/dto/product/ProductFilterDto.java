package com.predators.dto.product;

import java.math.BigDecimal;

public record ProductFilterDto (BigDecimal minPrice,
                                BigDecimal maxPrice,
                                Boolean discount,
                                Long categoryId) {
}
