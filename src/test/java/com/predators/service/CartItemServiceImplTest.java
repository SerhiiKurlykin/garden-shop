package com.predators.service;

import com.predators.entity.Cart;
import com.predators.entity.CartItem;
import com.predators.entity.Product;
import com.predators.repository.CartItemJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartItemServiceImplTest {

    @Mock
    private CartItemJpaRepository cartItemJpaRepository;

    @InjectMocks
    private CartItemServiceImpl cartItemService;

    private CartItem testItem;

    @BeforeEach
    void setUp() {
        Cart cart = new Cart();
        cart.setId(1L);
        Product product = new Product();
        product.setId(2L);

        testItem = CartItem.builder()
                .id(10L)
                .cart(cart)
                .product(product)
                .quantity(3)
                .build();
    }

    @Test
    void getAll_ShouldReturnAllCartItems() {
        List<CartItem> items = Arrays.asList(testItem);
        when(cartItemJpaRepository.findAll()).thenReturn(items);

        List<CartItem> result = cartItemService.getAll();

        assertEquals(1, result.size());
        assertEquals(testItem, result.get(0));
    }

    @Test
    void getById_WhenFound_ShouldReturnCartItem() {
        when(cartItemJpaRepository.findById(10L)).thenReturn(Optional.of(testItem));

        CartItem result = cartItemService.getById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
    }

    @Test
    void getById_WhenNotFound_ShouldThrowException() {
        when(cartItemJpaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                cartItemService.getById(99L));

        assertEquals("CartItem Not Found", exception.getMessage());
    }

    @Test
    void create_ShouldSaveAndReturnCartItem() {
        when(cartItemJpaRepository.save(testItem)).thenReturn(testItem);

        CartItem saved = cartItemService.create(testItem);

        assertNotNull(saved);
        assertEquals(testItem.getId(), saved.getId());
    }

    @Test
    void findByProductId_ShouldReturnCartItemOptional() {
        when(cartItemJpaRepository.findByProduct_Id(2L)).thenReturn(Optional.of(testItem));

        CartItem result = cartItemService.findByProduct_Id(2L);

        assertEquals(testItem.getProduct().getId(), result.getProduct().getId());
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoCartItemsExist() {
        when(cartItemJpaRepository.findAll()).thenReturn(List.of()); // Return empty list

        List<CartItem> result = cartItemService.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(cartItemJpaRepository, times(1)).findAll();
    }
}
