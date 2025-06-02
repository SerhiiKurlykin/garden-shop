package com.predators.service;

import com.predators.entity.Favorite;

import java.util.Set;

public interface FavoriteService {

    Set<Favorite> getAll();

    Favorite create(Long productId);

    Favorite getById(Long id);

    void delete(Long id);
}
