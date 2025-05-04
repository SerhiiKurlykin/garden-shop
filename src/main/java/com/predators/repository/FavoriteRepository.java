package com.predators.repository;

import com.predators.entity.Favorite;
import com.predators.entity.ShopUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByUser(ShopUser currentUser);
}
