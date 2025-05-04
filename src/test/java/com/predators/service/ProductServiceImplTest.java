package com.predators.service;

import com.predators.dto.converter.ProductConverter;
import com.predators.dto.product.ProductRequestDto;
import com.predators.entity.Category;
import com.predators.entity.Product;
import com.predators.exception.ProductNotFoundException;
import com.predators.repository.ProductJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductJpaRepository repository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductServiceImpl productService;

    @InjectMocks
    private ProductConverter converter;


    @Test
    public void testGetAll() {
        productService.getAll();
        verify(repository, times(1)).findAll();
    }


    @Test
    void testCreate() {
        Category mockCategory = new Category();
        mockCategory.setId(1L);

        when(categoryService.getById(1L)).thenReturn(mockCategory);

        Product entity = converter.toEntity(new ProductRequestDto("banana", "banana",
                new BigDecimal(15), 1L, "http/url"));
        productService.create(entity);
        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetById() {
        Long id = 1L;
        Product expectedProduct = new Product();
        when(repository.findById(id)).thenReturn(Optional.of(expectedProduct));

        Product actualProduct = productService.getById(id);

        assertEquals(expectedProduct, actualProduct);
        verify(repository, times(1)).findById(id);
    }

    @Test
    void testGetByIdNotFound() {
        assertThrows(ProductNotFoundException.class, () ->
                productService.getById(2L));
    }

    @Test
    void delete() {
        productService.delete(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}