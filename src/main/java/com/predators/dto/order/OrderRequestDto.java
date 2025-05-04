package com.predators.dto.order;

import com.predators.dto.cart.ProductToItemDto;
import lombok.Builder;
import java.util.List;

@Builder
public record OrderRequestDto(
        List<ProductToItemDto> items,
        String deliveryAddress,
        String deliveryMethod
) {
}
