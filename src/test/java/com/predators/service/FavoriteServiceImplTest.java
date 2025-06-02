package com.predators.service;

import com.predators.entity.Favorite;
import com.predators.entity.Product;
import com.predators.entity.ShopUser;
import com.predators.exception.FavoriteNotFoundException;
import com.predators.exception.ProductNotFoundException;
import com.predators.repository.FavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private ProductService productService;

    @Mock
    private ShopUserService shopUserService;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    private ShopUser user;
    private Product product;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        user = new ShopUser();
        user.setId(1L);
        product = new Product();
        product.setId(2L);

        favorite = Favorite.builder()
                .id(3L)
                .user(user)
                .product(product)
                .build();
    }

    @Test
    void testGetAllFavorites() {
        when(shopUserService.getCurrentUser()).thenReturn(user);
        when(favoriteRepository.findAllByUser(user)).thenReturn(Set.of(favorite));

        Set<Favorite> result = favoriteService.getAll();

        assertThat(result).hasSize(1);
        verify(favoriteRepository, times(1)).findAllByUser(user);
    }

    @Test
    void testCreateFavorite_NewFavorite() {
        when(shopUserService.getCurrentUser()).thenReturn(user);
        when(productService.getById(2L)).thenReturn(product);
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);

        Favorite result = favoriteService.create(2L);

        assertThat(result.getProduct().getId()).isEqualTo(2L);
        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }

    @Test
    void testGetFavoriteById_Found() {
        when(shopUserService.getCurrentUser()).thenReturn(user);
        when(favoriteRepository.findByIdAndUser(3L, user)).thenReturn(Optional.ofNullable(favorite));

        Favorite result = favoriteService.getById(3L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
    }

    @Test
    void testGetFavoriteById_NotFound() {
        when(shopUserService.getCurrentUser()).thenReturn(user);
        when(favoriteRepository.findByIdAndUser(99L, user)).thenReturn(Optional.empty());

        assertThrows(FavoriteNotFoundException.class, () -> favoriteService.getById(99L));
        verify(favoriteRepository, times(1)).findByIdAndUser(99L, user);
    }

    @Test
    void testDeleteFavorite_Success() {
        when(shopUserService.getCurrentUser()).thenReturn(user); // Для вызова getById
        when(favoriteRepository.findByIdAndUser(3L, user)).thenReturn(Optional.of(favorite)); // getById будет использовать это

        doNothing().when(favoriteRepository).deleteById(3L);

        favoriteService.delete(3L);

        verify(shopUserService, times(1)).getCurrentUser();
        verify(favoriteRepository, times(1)).findByIdAndUser(3L, user); // Убеждаемся, что getById был вызван и нашел избранное
        verify(favoriteRepository, times(1)).deleteById(3L); // Убеждаемся, что deleteById был вызван
    }

    @Test
    void testDeleteFavorite_NotFoundThrowsException() {
        when(shopUserService.getCurrentUser()).thenReturn(user);
        when(favoriteRepository.findByIdAndUser(99L, user)).thenReturn(Optional.empty());

        assertThrows(FavoriteNotFoundException.class, () -> favoriteService.delete(99L));

        verify(shopUserService, times(1)).getCurrentUser();
        verify(favoriteRepository, times(1)).findByIdAndUser(99L, user);
        verify(favoriteRepository, never()).deleteById(anyLong());
    }

    @Test
    void testCreateFavorite_ProductNotFound() {
        when(shopUserService.getCurrentUser()).thenReturn(user);
        when(productService.getById(99L)).thenThrow(new ProductNotFoundException("Product not found"));

        assertThrows(ProductNotFoundException.class, () -> favoriteService.create(99L));

        verify(shopUserService, times(1)).getCurrentUser();
        verify(productService, times(1)).getById(99L);
        verify(favoriteRepository, never()).save(any(Favorite.class));
    }
}
