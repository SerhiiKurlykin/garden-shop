package com.predators.controller;

import com.predators.dto.cart.ProductToItemDto;
import com.predators.dto.cartItem.CartItemMapper;
import com.predators.dto.cartItem.CartItemResponseDto;
import com.predators.entity.Cart;
import com.predators.entity.CartItem;
import com.predators.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/cart")
@RequiredArgsConstructor
public class CartController implements CartApi {

    private final CartService service;

    private final CartItemMapper cartItemMapper;

    @GetMapping("/all")
    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> allCarts = service.getAll();
        return new ResponseEntity<>(allCarts, HttpStatus.OK);
    }

    @GetMapping
    @Override
    public ResponseEntity<Set<CartItemResponseDto>> getAllCartItems() {

        Set<CartItemResponseDto> collect = service.getAllCartItems()
                .stream().map(cartItemMapper::toDto).collect(Collectors.toSet());

        return ResponseEntity.ok(collect);
    }

    @PostMapping
    @Override
    public ResponseEntity<CartItem> addProduct(@RequestBody ProductToItemDto productToItemDto) {
        return ResponseEntity.ok(service.addProduct(productToItemDto));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") Long productId) {
        service.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }
}
