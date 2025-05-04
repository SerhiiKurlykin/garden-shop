package com.predators.controller;

import com.predators.dto.converter.FavoriteConverter;
import com.predators.dto.favorite.FavoriteResponseDto;
import com.predators.entity.Favorite;
import com.predators.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("v1/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorites", description = "Operations related to user's favorite products")
public class FavoriteController {

    private final FavoriteService favoriteService;

    private final FavoriteConverter favoriteConverter;

    @GetMapping
    @Operation(summary = "Get all favorites", description = "Retrieves a list of all favorite items for the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of favorites",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = FavoriteResponseDto.class))))
    public ResponseEntity<List<FavoriteResponseDto>> getAll() {
        return new ResponseEntity<>(favoriteService.getAll().stream()
                .map(favoriteConverter::toDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    @Operation(summary = "Add a product to favorites", description = "Adds a specific product to the authenticated user's favorites.")
    @Parameter(name = "id", description = "ID of the product to add to favorites", required = true, schema = @Schema(type = "integer", format = "int64", example = "456"))
    @ApiResponse(responseCode = "201", description = "Successfully added the product to favorites",
            content = @Content(schema = @Schema(implementation = FavoriteResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Product with the given ID not found")
    public ResponseEntity<FavoriteResponseDto> create(@PathVariable(name = "id") Long productId) {
        Favorite favorite = favoriteService.create(productId);
        return new ResponseEntity<>(favoriteConverter.toDto(favorite), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a favorite item by ID", description = "Retrieves a specific favorite item based on its unique identifier.")
    @Parameter(name = "id", description = "ID of the favorite item to retrieve",
            required = true, schema = @Schema(type = "integer", format = "int64", example = "123"))
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the favorite item",
            content = @Content(schema = @Schema(implementation = FavoriteResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Favorite item with the given ID not found")
    public ResponseEntity<FavoriteResponseDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(favoriteConverter.toDto(favoriteService.getById(id)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove a favorite item", description = "Removes a specific favorite item based on its unique identifier.")
    @Parameter(name = "id", description = "ID of the favorite item to remove", required = true, schema = @Schema(type = "integer", format = "int64", example = "123"))
    @ApiResponse(responseCode = "200", description = "Successfully removed the favorite item")
    @ApiResponse(responseCode = "404", description = "Favorite item with the given ID not found")
    public void delete(@PathVariable Long id) {
        favoriteService.delete(id);
    }
}

