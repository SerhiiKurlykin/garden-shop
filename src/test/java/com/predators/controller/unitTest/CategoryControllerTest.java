package com.predators.controller.unitTest;

import com.predators.controller.CategoryController;
import com.predators.dto.category.CategoryRequestDto;
import com.predators.dto.category.CategoryResponseDto;
import com.predators.dto.converter.CategoryConverter;
import com.predators.entity.Category;
import com.predators.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private CategoryConverter categoryConverter;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories() {
        Category category = new Category();
        CategoryResponseDto dto = new CategoryResponseDto(1L,"one");

        when(categoryService.getAll()).thenReturn(List.of(category));
        when(categoryConverter.toDto(category)).thenReturn(dto);

        ResponseEntity<List<CategoryResponseDto>> response = categoryController.getAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(categoryService, times(1)).getAll();
    }

    @Test
    void testGetCategoryById() {
        Long id = 1L;
        Category category = new Category();
        CategoryResponseDto dto = new CategoryResponseDto(1L,"one");

        when(categoryService.getById(id)).thenReturn(category);
        when(categoryConverter.toDto(category)).thenReturn(dto);

        ResponseEntity<CategoryResponseDto> response = categoryController.getById(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
        verify(categoryService).getById(id);
    }

    @Test
    void testCreateCategory() {
        CategoryRequestDto requestDto = new CategoryRequestDto("one");
        Category category = new Category();
        Category createdCategory = new Category();
        CategoryResponseDto responseDto = new CategoryResponseDto(1L,"one");

        when(categoryConverter.toEntity(requestDto)).thenReturn(category);
        when(categoryService.create(category)).thenReturn(createdCategory);
        when(categoryConverter.toDto(createdCategory)).thenReturn(responseDto);

        ResponseEntity<CategoryResponseDto> response = categoryController.create(requestDto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(responseDto, response.getBody());
        verify(categoryService).create(category);
    }

    @Test
    void testDeleteCategory() {
        Long id = 1L;

        ResponseEntity<Void> response = categoryController.delete(id);

        assertEquals(200, response.getStatusCodeValue());
        verify(categoryService).delete(id);
    }
}
