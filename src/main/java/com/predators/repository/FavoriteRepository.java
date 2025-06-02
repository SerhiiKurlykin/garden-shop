package com.predators.repository;

import com.predators.entity.Favorite;
import com.predators.entity.Product;
import com.predators.entity.ShopUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Set<Favorite> findAllByUser(ShopUser currentUser);

    Optional<Favorite> findByIdAndUser(Long id, ShopUser currentUser);

    Optional<Favorite> findByUserAndProduct(ShopUser currentUser, Product product);
}
