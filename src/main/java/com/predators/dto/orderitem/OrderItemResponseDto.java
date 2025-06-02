package com.predators.dto.orderitem;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemResponseDto(Long id,
                                   Long productId,
                                   int quantity,
                                   BigDecimal priceAtPurchase) {
}
