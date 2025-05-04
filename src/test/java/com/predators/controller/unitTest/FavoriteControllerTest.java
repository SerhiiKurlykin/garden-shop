package com.predators.controller.unitTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.predators.controller.FavoriteController;
import com.predators.dto.converter.FavoriteConverter;
import com.predators.dto.favorite.FavoriteResponseDto;
import com.predators.entity.Favorite;
import com.predators.security.JwtAuthFilter;
import com.predators.security.JwtService;
import com.predators.service.FavoriteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoriteController.class)
@AutoConfigureMockMvc(addFilters = false)
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private FavoriteConverter favoriteConverter;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllFavorites() throws Exception {
        Favorite favorite = new Favorite();
        FavoriteResponseDto favoriteDto = new FavoriteResponseDto(1L, 2L, 3L);

        when(favoriteService.getAll()).thenReturn(List.of(favorite));
        when(favoriteConverter.toDto(any(Favorite.class))).thenReturn(favoriteDto);

        mockMvc.perform(get("/v1/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(favoriteService, times(1)).getAll();
    }

    @Test
    void testCreateFavorite() throws Exception {
        Favorite favorite = new Favorite();
        FavoriteResponseDto favoriteDto = new FavoriteResponseDto(1L, 2L, 3L);

        when(favoriteService.create(1L)).thenReturn(favorite);
        when(favoriteConverter.toDto(any(Favorite.class))).thenReturn(favoriteDto);

        mockMvc.perform(post("/v1/favorites/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(favoriteService, times(1)).create(1L);
    }

    @Test
    void testGetFavoriteById() throws Exception {
        Favorite favorite = new Favorite();
        FavoriteResponseDto favoriteDto = new FavoriteResponseDto(1L, 2L, 3L);

        when(favoriteService.getById(1L)).thenReturn(favorite);
        when(favoriteConverter.toDto(any(Favorite.class))).thenReturn(favoriteDto);

        mockMvc.perform(get("/v1/favorites/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(favoriteService, times(1)).getById(1L);
    }

    @Test
    void testDeleteFavorite() throws Exception {
        doNothing().when(favoriteService).delete(1L);

        mockMvc.perform(delete("/v1/favorites/1"))
                .andExpect(status().isOk());

        verify(favoriteService, times(1)).delete(1L);
    }
}
