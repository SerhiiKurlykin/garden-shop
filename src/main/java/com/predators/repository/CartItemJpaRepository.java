package com.predators.repository;

import com.predators.entity.Cart;
import com.predators.entity.CartItem;
import com.predators.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemJpaRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByProduct_Id(Long productId);

    Optional<CartItem> findCartItemByProductAndCart(Product product, Cart cart);

    Optional<CartItem> findCartItemByProduct_IdAndCart_Id(Long productId, Long cartId);
}
