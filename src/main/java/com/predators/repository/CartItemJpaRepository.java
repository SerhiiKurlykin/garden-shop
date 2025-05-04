package com.predators.repository;

import com.predators.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemJpaRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByProduct_Id(Long productId);
}
