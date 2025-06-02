package com.predators.service;

import com.predators.dto.cart.ProductToItemDto;
import com.predators.entity.Cart;
import com.predators.entity.CartItem;

import java.util.List;
import java.util.Set;

public interface CartService {

    List<Cart> getAll();

    Set<CartItem> getAllCartItems();

    CartItem addProduct(ProductToItemDto productToItemDto);

    void deleteProduct(Long productId);

    Cart save(Cart cart);
}
