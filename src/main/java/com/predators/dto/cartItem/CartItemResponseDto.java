package com.predators.dto.cartItem;

public record CartItemResponseDto(Long id,
                                  Long cartId,
                                  Long productId,
                                  String productName,
                                  int quantity) {
}
