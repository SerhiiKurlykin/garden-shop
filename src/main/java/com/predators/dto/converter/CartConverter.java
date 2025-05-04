package com.predators.dto.converter;

import com.predators.dto.cart.ProductToItemDto;
import com.predators.dto.cart.CartResponseDto;
import com.predators.entity.Cart;
import org.springframework.stereotype.Component;

@Component
public class CartConverter implements Converter<ProductToItemDto, CartResponseDto, Cart> {

    @Override
    public CartResponseDto toDto(Cart cart) {
        return null;
    }

    @Override
    public Cart toEntity(ProductToItemDto productToItemDto) {

        return null;
    }
}
