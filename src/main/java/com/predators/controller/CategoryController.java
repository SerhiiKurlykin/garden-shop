package com.predators.controller;

import com.predators.dto.category.CategoryRequestDto;
import com.predators.dto.category.CategoryResponseDto;
import com.predators.dto.converter.CategoryConverter;
import com.predators.entity.Category;
import com.predators.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/categories")
public class CategoryController {

    private final CategoryService service;

    private final CategoryConverter converter;

    public CategoryController(CategoryService categoryService, CategoryConverter converter) {
        this.service = categoryService;
        this.converter = converter;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CategoryResponseDto>> getAll() {
        List<CategoryResponseDto> dtolist = service.getAll().stream()
                .map(converter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(dtolist, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable Long id) {
        Category category = service.getById(id);
        return new ResponseEntity<>(converter.toDto(category), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CategoryResponseDto> create(@RequestBody CategoryRequestDto categoryDto) {
        Category category = converter.toEntity(categoryDto);
        Category createdCategory = service.create(category);
        return new ResponseEntity<>(converter.toDto(createdCategory), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CategoryResponseDto> update(@PathVariable(name = "id") Long id, @RequestParam String name) {
        Category category = service.update(id, name);
        return new ResponseEntity<>(converter.toDto(category), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}