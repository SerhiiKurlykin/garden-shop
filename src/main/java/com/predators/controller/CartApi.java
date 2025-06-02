package com.predators.controller;

import com.predators.dto.cart.ProductToItemDto;
import com.predators.dto.cartItem.CartItemResponseDto;
import com.predators.dto.product.ProductResponseDto;
import com.predators.entity.Cart;
import com.predators.entity.CartItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@Tag(name = "Cart Management", description = "Operations related to shopping cart functionality")
public interface CartApi {

    @Operation(summary = "Get all carts (Admin only)",
            description = "Retrieves all shopping carts in the system. Requires ADMIN role.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all carts",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Cart.class))))
    @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN privileges")
    ResponseEntity<List<Cart>> getAllCarts();

    @Operation(summary = "Get current user's cart products",
            description = "Retrieves all products in the authenticated user's shopping cart")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved cart products",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class))))
    ResponseEntity<Set<CartItemResponseDto>> getAllCartItems();

    @PostMapping
    @Operation(summary = "Add product to cart",
            description = "Adds a product with specified quantity to the user's shopping cart")
    @ApiResponse(responseCode = "200", description = "Successfully added product to cart",
            content = @Content(schema = @Schema(implementation = CartItem.class)))
    @ApiResponse(responseCode = "404", description = "Product not found")
    ResponseEntity<CartItem> addProduct(@RequestBody ProductToItemDto productToItemDto);

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove product from cart",
            description = "Removes a specific product from the user's shopping cart")
    @Parameter(name = "id", description = "ID of the product to remove", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "123"))
    @ApiResponse(responseCode = "200", description = "Successfully removed product from cart")
    @ApiResponse(responseCode = "404", description = "Product not found in cart")
    ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") Long productId);
}
