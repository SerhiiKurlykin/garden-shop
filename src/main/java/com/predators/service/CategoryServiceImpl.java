package com.predators.service;

import com.predators.entity.Category;
import com.predators.entity.Product;
import com.predators.exception.CategoryNotFoundException;
import com.predators.repository.CategoryJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryJpaRepository repository;

    @Override
    public List<Category> getAll() {
        return repository.findAll();
    }

    @Override
    public Category getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }

    @Override
    public Category create(Category category) {
        return repository.save(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new CategoryNotFoundException("Category not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public Category update(Long id, String name) {
        Category category = getById(id);
        category.setName(name);
        return create(category);
    }
}