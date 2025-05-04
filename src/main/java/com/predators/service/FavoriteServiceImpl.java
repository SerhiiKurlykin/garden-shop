package com.predators.service;

import com.predators.entity.Favorite;
import com.predators.entity.Product;
import com.predators.entity.ShopUser;
import com.predators.exception.FavoriteNotFoundException;
import com.predators.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final ProductService productService;

    private final ShopUserService shopUserService;

    @Override
    public List<Favorite> getAll() {
        ShopUser currentUser = shopUserService.getCurrentUser();

        return favoriteRepository.findAllByUser(currentUser);
    }

    @Override
    public Favorite create(Long productId) {
        ShopUser currentUser = shopUserService.getCurrentUser();
        Product byId = productService.getById(productId);
        List<Favorite> favorites = currentUser.getFavorites();

        for (Favorite favorite : favorites) {
            Long favoriteProductId = favorite.getProduct().getId();
            if (favoriteProductId.equals(productId)) {
                return favoriteRepository.save(favorite);
            }
        }

        Favorite favorite = Favorite.builder()
                .user(currentUser)
                .product(byId)
                .build();
        return favoriteRepository.save(favorite);
    }

    @Override
    public Favorite getById(Long id) {
        ShopUser currentUser = shopUserService.getCurrentUser();
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new FavoriteNotFoundException("Favorite not found with id: " + id));
        if (favorite.getUser().getId().equals(currentUser.getId())) {
            return favorite;
        }
        throw new FavoriteNotFoundException("Favorite not found with id: " + id);
    }

    @Override
    public void delete(Long id) {
        favoriteRepository.deleteById(id);
    }
}
