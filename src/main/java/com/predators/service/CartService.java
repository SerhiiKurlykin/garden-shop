package com.predators.service;

import com.predators.dto.cart.ProductToItemDto;
import com.predators.entity.Cart;
import com.predators.entity.CartItem;
import com.predators.entity.Product;

import java.util.List;

public interface CartService {

    List<Cart> getAll();

    List<Product> getAllProducts();

    CartItem addProduct(ProductToItemDto productToItemDto);

    void deleteProduct(Long productId);

    void deleteById(Long id);
}
