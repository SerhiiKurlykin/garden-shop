package com.predators.service;

import com.jayway.jsonpath.PathNotFoundException;
import com.predators.dto.cart.ProductToItemDto;
import com.predators.entity.Cart;
import com.predators.entity.CartItem;
import com.predators.entity.Product;
import com.predators.entity.ShopUser;
import com.predators.exception.CartIsEmptyException;
import com.predators.exception.NotCurrentClientCartException;
import com.predators.repository.CartJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    public List<Product> getAllProducts() {
        ShopUser currentUser = shopUserService.getCurrentUser();
        if (currentUser.getCart() == null) {
            throw new CartIsEmptyException("Cart is empty.");
        }

        return currentUser.getCart().getCartItems().stream().map(CartItem::getProduct).toList();
    }

    @Override
    @Transactional
    public CartItem addProduct(ProductToItemDto productToItemDto) {
        ShopUser currentUser = shopUserService.getCurrentUser();
        createCartIfNotExists(currentUser);
        Product product = productService.getById(productToItemDto.productId());
        List<CartItem> cartItems = currentUser.getCart().getCartItems();
       for (CartItem cartItem : cartItems) {
           boolean equals = cartItem.getProduct().getId().equals(productToItemDto.productId());
           if (equals) {
               cartItem.setQuantity(productToItemDto.quantity());
               return cartItemService.create(cartItem);
           }
       }
        return cartItemService.create(CartItem.builder()
                .cart(currentUser.getCart())
                .product(product)
                .quantity(productToItemDto.quantity())
                .build());
    }

    private void createCartIfNotExists(ShopUser currentUser) {
        if (currentUser.getCart() == null) {
            List<CartItem> cartItems = new ArrayList<>();
            Cart cart = Cart.builder()
                    .user(currentUser)
                    .cartItems(cartItems)
                    .build();
            repository.save(cart);
            currentUser.setCart(cart);
        }
    }

    @Override
    public void deleteProduct(Long productId) {
        Optional<CartItem> cartItemByProduct = cartItemService.findByProduct_Id(productId);
        if (cartItemByProduct.isEmpty()) {
            throw new PathNotFoundException("Product with id " + productId + " not found");
        }

        ShopUser user = shopUserService.getCurrentUser();
        Cart cart = cartItemByProduct.get().getCart();
        if (!Objects.equals(user.getCart().getId(), cart.getId())) {
            throw new NotCurrentClientCartException("This is not your Cart. Finger weg!");
        }

        cartItemService.delete(cartItemByProduct.get().getId());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<Cart> cartIf = repository.findById(id);
        if (cartIf.isPresent()) {
            repository.delete(cartIf.get());
        }
    }
}
