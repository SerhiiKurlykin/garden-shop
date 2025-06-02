package com.predators.controller.unitTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.predators.controller.FavoriteController;
import com.predators.dto.favorite.FavoriteMapper;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FavoriteController.class)
@AutoConfigureMockMvc(addFilters = false)
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private FavoriteMapper mapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = {"ROman"})
    void testGetAllFavorites() throws Exception {
        Favorite favorite = new Favorite();
        FavoriteResponseDto favoriteDto = new FavoriteResponseDto(1L, 2L, 3L);

        when(favoriteService.getAll()).thenReturn(Set.of(favorite));
        when(mapper.toDto(any(Favorite.class))).thenReturn(favoriteDto);

        mockMvc.perform(get("/v1/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testCreateFavorite() throws Exception {
        Favorite favorite = new Favorite();
        FavoriteResponseDto favoriteDto = new FavoriteResponseDto(1L, 2L, 3L);

        when(favoriteService.create(1L)).thenReturn(favorite);
        when(mapper.toDto(any(Favorite.class))).thenReturn(favoriteDto);

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
        when(mapper.toDto(any(Favorite.class))).thenReturn(favoriteDto);

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
