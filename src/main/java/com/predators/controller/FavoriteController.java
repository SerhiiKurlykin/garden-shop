package com.predators.controller;

import com.predators.dto.favorite.FavoriteMapper;
import com.predators.dto.favorite.FavoriteResponseDto;
import com.predators.entity.Favorite;
import com.predators.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("v1/favorites")
@RequiredArgsConstructor
public class FavoriteController implements FavoriteApi {

    private final FavoriteService favoriteService;

    private final FavoriteMapper favoriteMapper;

    @GetMapping
    @Override
    public ResponseEntity<List<FavoriteResponseDto>> getAll() {
        return new ResponseEntity<>(favoriteService.getAll().stream()
                .map(favoriteMapper::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    @Override
    public ResponseEntity<FavoriteResponseDto> create(@PathVariable(name = "id") Long productId) {
        Favorite favorite = favoriteService.create(productId);
        return new ResponseEntity<>(favoriteMapper.toDto(favorite), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<FavoriteResponseDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(favoriteMapper.toDto(favoriteService.getById(id)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Override
    public void delete(@PathVariable Long id) {
        favoriteService.delete(id);
    }
}

