package com.predators.service;

import com.predators.entity.CartItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CartItemService {

    List<CartItem> getAll();

    CartItem getById(Long id);

    CartItem create(CartItem cartItem);

    void delete(Long id);

    Optional<CartItem> findByProduct_Id(Long productId);
}

