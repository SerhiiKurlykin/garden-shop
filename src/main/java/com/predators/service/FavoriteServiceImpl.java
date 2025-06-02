package com.predators.service;

import com.predators.entity.Favorite;
import com.predators.entity.Product;
import com.predators.entity.ShopUser;
import com.predators.exception.FavoriteAlreadyExistsException;
import com.predators.exception.FavoriteNotFoundException;
import com.predators.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final ProductService productService;

    private final ShopUserService shopUserService;

    @Override
    public Set<Favorite> getAll() {
        ShopUser currentUser = shopUserService.getCurrentUser();
        return favoriteRepository.findAllByUser(currentUser);
    }

    @Override
    public Favorite create(Long productId) {
        ShopUser currentUser = shopUserService.getCurrentUser();
        Product product = productService.getById(productId);
        Set<Favorite> favorites = currentUser.getFavorites();
        Favorite existedFavorite = favoriteRepository.findByUserAndProduct(currentUser, product).orElse(null);

        if (favorites.contains(existedFavorite)) {
            throw new FavoriteAlreadyExistsException("Favorite with such data already exists");
        }

        existedFavorite = Favorite.builder()
                .user(currentUser)
                .product(product)
                .build();

        favorites.add(existedFavorite);

        return favoriteRepository.save(existedFavorite);
    }

    @Override
    public Favorite getById(Long id) {
        ShopUser currentUser = shopUserService.getCurrentUser();
        return favoriteRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new FavoriteNotFoundException("Favorite not found with id: " + id));
    }

    @Override
    public void delete(Long id) {
        Favorite favorite = getById(id);
        favoriteRepository.deleteById(favorite.getId());
    }
}
