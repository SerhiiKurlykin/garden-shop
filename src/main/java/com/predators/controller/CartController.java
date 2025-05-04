package com.predators.controller;

import com.predators.dto.cart.ProductToItemDto;
import com.predators.dto.converter.ProductConverter;
import com.predators.dto.product.ProductResponseDto;
import com.predators.entity.Cart;
import com.predators.entity.CartItem;
import com.predators.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService service;

    private final ProductConverter productConverter;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Cart>> getAllCarts() {
        List<Cart> allCarts = service.getAll();
        return new ResponseEntity<>(allCarts, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> productsResponse =
                service.getAllProducts().stream().map(productConverter::toDto).toList();
        return ResponseEntity.ok(productsResponse);
    }

    @PostMapping
    public ResponseEntity<CartItem> addProduct(@RequestBody ProductToItemDto productToItemDto) {
        return ResponseEntity.ok(service.addProduct(productToItemDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") Long productId) {
        service.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/current-cart/{id}")
    public ResponseEntity<Void> deleteCartById(@PathVariable(name = "id") Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
