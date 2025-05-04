package com.predators.service;

import com.predators.entity.Favorite;
import com.predators.entity.Product;
import com.predators.entity.ShopUser;
import com.predators.exception.FavoriteNotFoundException;
import com.predators.repository.FavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
        user.setFavorites(new ArrayList<>());

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
        when(favoriteRepository.findAll()).thenReturn(List.of(favorite));

        List<Favorite> result = favoriteService.getAll();

        assertThat(result).hasSize(1);
        verify(favoriteRepository, times(1)).findAll();
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
        when(favoriteRepository.findById(3L)).thenReturn(Optional.of(favorite));

        Favorite result = favoriteService.getById(3L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        verify(favoriteRepository, times(1)).findById(3L);
    }

    @Test
    void testGetFavoriteById_NotFound() {
        when(favoriteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(FavoriteNotFoundException.class, () -> favoriteService.getById(99L));
        verify(favoriteRepository, times(1)).findById(99L);
    }

    @Test
    void testDeleteFavorite() {
        doNothing().when(favoriteRepository).deleteById(3L);

        favoriteService.delete(3L);

        verify(favoriteRepository, times(1)).deleteById(3L);
    }
}
