package com.predators.service;

import com.predators.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAll();

    Category getById(Long id);

    Category create(Category category);

    void delete(Long id);

    Category update(Long id, String name);
}