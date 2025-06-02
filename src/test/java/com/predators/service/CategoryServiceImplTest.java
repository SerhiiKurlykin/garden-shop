package com.predators.service;

import com.predators.entity.Category;
import com.predators.exception.CategoryNotFoundException;
import com.predators.repository.CategoryJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryJpaRepository categoryJpaRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1L)
                .name("GARDEN_TOOLS")
                .build();
    }

    @Test
    void testGetAll() {
        List<Category> categories = Arrays.asList(category);
        when(categoryJpaRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAll();
        assertEquals(1, result.size());
        assertEquals("GARDEN_TOOLS", result.get(0).getName());
    }

    @Test
    void testGetById_WhenCategoryExists() {
        when(categoryJpaRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.getById(1L);
        assertEquals("GARDEN_TOOLS", result.getName());
    }

    @Test
    void testGetById_WhenCategoryDoesNotExist() {
        when(categoryJpaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.getById(1L));
    }

    @Test
    void testCreate() {
        when(categoryJpaRepository.save(category)).thenReturn(category);

        Category result = categoryService.create(category);
        assertEquals("GARDEN_TOOLS", result.getName());
    }

    @Test
    void testDelete_WhenCategoryDoesNotExist() {
        when(categoryJpaRepository.findById(1L)).thenThrow(CategoryNotFoundException.class);

        assertThrows(CategoryNotFoundException.class, () -> categoryService.delete(1L));
    }
}