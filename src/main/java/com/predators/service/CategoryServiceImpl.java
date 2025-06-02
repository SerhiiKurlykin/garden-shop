package com.predators.service;

import com.predators.entity.Category;
import com.predators.exception.CategoryAlreadyExistsException;
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
        if (repository.findByName(category.getName()).isPresent()) {
            throw new CategoryAlreadyExistsException("Category already exists with name: " + category.getName());
        }
        return repository.save(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = getById(id);
        repository.deleteById(category.getId());
    }

    @Override
    public Category update(Long id, String name) {
        Category category = getById(id);
        category.setName(name);
        return create(category);
    }
}