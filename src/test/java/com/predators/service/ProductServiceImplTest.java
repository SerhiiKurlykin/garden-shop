package com.predators.service;

import com.predators.dto.product.ProductFilterDto;
import com.predators.dto.product.ProductRequestDto;
import com.predators.entity.Category;
import com.predators.entity.Product;
import com.predators.exception.DiscountNotFoundException;
import com.predators.exception.ProductNotFoundException;
import com.predators.repository.ProductJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductJpaRepository repository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
    }

    @Test
    void getAll_shouldReturnListOfProducts() {
        List<Product> products = List.of(new Product(), new Product());
        when(repository.findAll()).thenReturn(products);

        List<Product> result = productService.getAll();

        assertEquals(products.size(), result.size());
        assertEquals(products, result);
        verify(repository, times(1)).findAll();
    }

    @Test
    void getAll_withInvalidSortDirection_shouldThrowIllegalArgumentException() {
        ProductFilterDto filter = ProductFilterDto.builder().build();
        int page = 0;
        int size = 10;
        String[] sortParams = {"name;INVALID"};

        assertThrows(IllegalArgumentException.class, () -> productService.getAll(filter, page, size, sortParams));
        verify(repository, never()).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void create_shouldSaveProductWithCreatedAt() {
        Product productToCreate = new Product();
        Product savedProduct = new Product();
        savedProduct.setId(1L);
        when(repository.save(any(Product.class))).thenReturn(savedProduct);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        Product result = productService.create(productToCreate);

        assertEquals(savedProduct, result);
        verify(repository, times(1)).save(productCaptor.capture());
        assertNotNull(productCaptor.getValue().getCreatedAt());
    }

    @Test
    void getById_shouldReturnProductIfExists() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        when(repository.findById(productId)).thenReturn(Optional.of(product));

        Product result = productService.getById(productId);

        assertEquals(product, result);
        verify(repository, times(1)).findById(productId);
    }

    @Test
    void getById_shouldThrowProductNotFoundExceptionIfNotExists() {
        Long productId = 1L;
        when(repository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getById(productId));
        verify(repository, times(1)).findById(productId);
    }

    @Test
    void update_shouldUpdateExistingProduct() {
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        ProductRequestDto updateDto = new ProductRequestDto("Updated Name", "Updated Description", BigDecimal.TEN, 2L, "updated.jpg");
        Category category = new Category();
        category.setId(2L);
        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Updated Name");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(BigDecimal.TEN);
        updatedProduct.setImageUrl("updated.jpg");
        updatedProduct.setCategory(category);

        when(repository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(categoryService.getById(2L)).thenReturn(category);
        when(repository.save(any(Product.class))).thenReturn(updatedProduct);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        Product result = productService.update(productId, updateDto);

        assertEquals(updatedProduct, result);
        verify(repository, times(2)).findById(productId);
        verify(categoryService, times(1)).getById(2L);
        verify(repository, times(2)).save(productCaptor.capture());
        Product capturedProduct = productCaptor.getValue();
        assertEquals("Updated Name", capturedProduct.getName());
        assertEquals("Updated Description", capturedProduct.getDescription());
        assertEquals(BigDecimal.TEN, capturedProduct.getPrice());
        assertEquals("updated.jpg", capturedProduct.getImageUrl());
        assertEquals(category, capturedProduct.getCategory());
        assertNotNull(capturedProduct.getUpdatedAt());
    }

    @Test
    void update_shouldNotUpdateFieldsIfNullOrBlank() {
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Original Name");
        existingProduct.setDescription("Original Description");
        existingProduct.setPrice(BigDecimal.ONE);
        existingProduct.setImageUrl("original.jpg");

        ProductRequestDto updateDtoWithNulls = new ProductRequestDto(null, " ", null, null, null);

        when(repository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(repository.save(any(Product.class))).thenReturn(existingProduct);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        Product result = productService.update(productId, updateDtoWithNulls);

        assertEquals(existingProduct, result);
        verify(repository, times(1)).findById(productId);
        verify(repository, times(1)).save(productCaptor.capture());
        Product capturedProduct = productCaptor.getValue();
        assertEquals("Original Name", capturedProduct.getName());
        assertEquals("Original Description", capturedProduct.getDescription());
        assertEquals(BigDecimal.ONE, capturedProduct.getPrice());
        assertEquals("original.jpg", capturedProduct.getImageUrl());
        assertNull(capturedProduct.getCategory());
        assertNotNull(capturedProduct.getUpdatedAt());
    }

    @Test
    void updateCategory_shouldUpdateProductCategory() {
        Long productId = 1L;
        Category newCategory = new Category();
        newCategory.setId(2L);
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        when(repository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(repository.save(existingProduct)).thenReturn(existingProduct);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        productService.updateCategory(productId, newCategory);

        assertEquals(newCategory, existingProduct.getCategory());
        verify(repository, times(1)).findById(productId);
        verify(repository, times(1)).save(productCaptor.capture());
        assertEquals(newCategory, productCaptor.getValue().getCategory());
    }

    @Test
    void findByCategoryId_shouldReturnListOfProductsForCategory() {
        Long categoryId = 1L;
        List<Product> products = List.of(new Product(), new Product());
        when(repository.findByCategory_Id(categoryId)).thenReturn(products);

        List<Product> result = productService.findByCategoryId(categoryId);

        assertEquals(products, result);
        verify(repository, times(1)).findByCategory_Id(categoryId);
    }

    @Test
    void getDayProduct_shouldThrowDiscountNotFoundExceptionIfNoDiscountedProducts() {
        when(repository.findAllByDiscountPriceIsNotNull()).thenReturn(List.of());

        assertThrows(DiscountNotFoundException.class, () -> productService.getDayProduct());
        verify(repository, times(1)).findAllByDiscountPriceIsNotNull();
    }

    @Test
    void getDayProduct_shouldHandleSingleProductWithDiscount() {
        BigDecimal discount = BigDecimal.valueOf(7.50);
        Product product = new Product();
        product.setId(1L);
        product.setDiscountPrice(discount);
        when(repository.findAllByDiscountPriceIsNotNull()).thenReturn(List.of(product));

        Product result = productService.getDayProduct();

        assertEquals(discount, result.getDiscountPrice());
        assertEquals(product.getId(), result.getId());
        verify(repository, times(1)).findAllByDiscountPriceIsNotNull();
    }

    @Test
    void deleteById_shouldDeleteProductIfExists() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);

        when(repository.findById(productId)).thenReturn(Optional.of(product));

        productService.delete(productId);

        verify(repository, times(1)).findById(productId);
        verify(repository, times(1)).deleteById(productId);
    }
}