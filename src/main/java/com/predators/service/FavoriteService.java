package com.predators.service;

import com.predators.entity.Favorite;

import java.util.List;

public interface FavoriteService {

    List<Favorite> getAll();

    Favorite create(Long productId);

    Favorite getById(Long id);

    void delete(Long id);
}
