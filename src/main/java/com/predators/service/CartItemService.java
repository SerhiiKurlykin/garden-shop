package com.predators.service;

import com.predators.entity.Cart;
import com.predators.entity.CartItem;
import com.predators.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartItemService {

    List<CartItem> getAll();

    CartItem getById(Long id);

    CartItem create(CartItem cartItem);

    void delete(Long id);

    CartItem findByProduct_Id(Long productId);

    CartItem findCartItemByProductAndCart(Product product, Cart cart);

    CartItem findByProductIdAndCartId(Long productId, Long cartId);
}

