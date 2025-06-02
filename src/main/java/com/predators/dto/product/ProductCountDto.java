package com.predators.dto.product;

import com.predators.entity.Product;

public record ProductCountDto(Product product, Long count) {
}
