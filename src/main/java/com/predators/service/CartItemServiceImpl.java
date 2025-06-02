package com.predators.service;

import com.predators.entity.Cart;
import com.predators.entity.CartItem;
import com.predators.entity.Product;
import com.predators.exception.CartItemNotFoundException;
import com.predators.repository.CartItemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemJpaRepository cartItemJpaRepository;

    @Override
    public List<CartItem> getAll() {
        return cartItemJpaRepository.findAll();
    }

    @Override
    public CartItem getById(Long id) {
        return cartItemJpaRepository.findById(id).
                orElseThrow(() -> new CartItemNotFoundException("CartItem Not Found"));
    }

    @Override
    public CartItem create(CartItem cartItem) {
        return cartItemJpaRepository.save(cartItem);
    }

    @Override
    public void delete(Long id) {
        CartItem cartItem = getById(id);
        cartItemJpaRepository.deleteById(cartItem.getId());
    }

    @Override
    public CartItem findByProduct_Id(Long productId) {
        return cartItemJpaRepository.findByProduct_Id(productId).orElseThrow(
                () -> new CartItemNotFoundException("Cart Item with product id " + productId + " not found.")
        );
    }

    @Override
    public CartItem findCartItemByProductAndCart(Product product, Cart cart) {
        return cartItemJpaRepository.findCartItemByProductAndCart(product, cart)
                .orElse(null);
    }

    @Override
    public CartItem findByProductIdAndCartId(Long productId, Long cartId) {
        return cartItemJpaRepository.findCartItemByProduct_IdAndCart_Id(productId, cartId)
                .orElseThrow(() -> new CartItemNotFoundException("No cartItem with such ids"));
    }
}
