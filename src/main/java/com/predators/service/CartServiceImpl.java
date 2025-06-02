package com.predators.service;

import com.predators.dto.cart.ProductToItemDto;
import com.predators.entity.Cart;
import com.predators.entity.CartItem;
import com.predators.entity.Product;
import com.predators.entity.ShopUser;
import com.predators.repository.CartJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartJpaRepository repository;

    private final ShopUserService shopUserService;

    private final ProductService productService;

    private final CartItemService cartItemService;

    @Override
    public List<Cart> getAll() {
        return repository.findAll();
    }

    @Override
    public Set<CartItem> getAllCartItems() {
        ShopUser currentUser = shopUserService.getCurrentUser();
        if (currentUser.getCart() == null) {
            return null;
        }
        return currentUser.getCart().getCartItems();
    }

    @Override
    @Transactional
    public CartItem addProduct(ProductToItemDto productToItemDto) {
        ShopUser currentUser = shopUserService.getCurrentUser();
        Cart cart = currentUser.getCart();
        if (cart == null) {
            cart = createCart(currentUser);
            currentUser.setCart(cart);
        }

        Product product = productService.getById(productToItemDto.productId());
        CartItem cartItem = cartItemService.findCartItemByProductAndCart(product, cart);
        if (cartItem == null) {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(productToItemDto.quantity())
                    .build();
        } else {
            cartItem.setQuantity(productToItemDto.quantity());
        }

        return cartItemService.create(cartItem);
    }

    private Cart createCart(ShopUser currentUser) {
        return repository.save(Cart.builder()
                .user(currentUser)
                .build());
    }

    @Override
    public void deleteProduct(Long productId) {
        Cart cart = shopUserService.getCurrentUser().getCart();
        CartItem cartItem = cartItemService.findByProductIdAndCartId(productId, cart.getId());

        cartItemService.delete(cartItem.getId());
    }

    @Override
    public Cart save(Cart cart) {
        return repository.save(cart);
    }
}
