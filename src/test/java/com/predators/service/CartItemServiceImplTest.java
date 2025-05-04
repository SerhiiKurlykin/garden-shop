package com.predators.service;

import com.predators.entity.CartItem;
import com.predators.entity.Cart;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void delete_WhenExists_ShouldDelete() {
        when(cartItemJpaRepository.existsById(10L)).thenReturn(true);

        cartItemService.delete(10L);

        verify(cartItemJpaRepository, times(1)).deleteById(10L);
    }

    @Test
    void delete_WhenNotExists_ShouldThrowException() {
        when(cartItemJpaRepository.existsById(99L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                cartItemService.delete(99L));

        assertEquals("CartItem Not Found", exception.getMessage());
    }

    @Test
    void findByProductId_ShouldReturnCartItemOptional() {
        when(cartItemJpaRepository.findByProduct_Id(2L)).thenReturn(Optional.of(testItem));

        Optional<CartItem> result = cartItemService.findByProduct_Id(2L);

        assertTrue(result.isPresent());
        assertEquals(testItem.getProduct().getId(), result.get().getProduct().getId());
    }
}
