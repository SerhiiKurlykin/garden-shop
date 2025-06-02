package com.predators.controller;

import com.predators.dto.favorite.FavoriteResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Favorites", description = "Operations related to user's favorite products")
public interface FavoriteApi {

    @Operation(summary = "Get all favorites", description = "Retrieves a list of all favorite items for the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of favorites",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = FavoriteResponseDto.class))))
    ResponseEntity<List<FavoriteResponseDto>> getAll();


    @Operation(summary = "Add a product to favorites", description = "Adds a specific product to the authenticated user's favorites.")
    @Parameter(name = "id", description = "ID of the product to add to favorites", required = true, schema = @Schema(type = "integer", format = "int64", example = "456"))
    @ApiResponse(responseCode = "201", description = "Successfully added the product to favorites",
            content = @Content(schema = @Schema(implementation = FavoriteResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Product with the given ID not found")
    ResponseEntity<FavoriteResponseDto> create(@PathVariable(name = "id") Long productId);


    @Operation(summary = "Get a favorite item by ID", description = "Retrieves a specific favorite item based on its unique identifier.")
    @Parameter(name = "id", description = "ID of the favorite item to retrieve",
            required = true, schema = @Schema(type = "integer", format = "int64", example = "123"))
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the favorite item",
            content = @Content(schema = @Schema(implementation = FavoriteResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Favorite item with the given ID not found")
    ResponseEntity<FavoriteResponseDto> getById(@PathVariable Long id);


    @Operation(summary = "Remove a favorite item", description = "Removes a specific favorite item based on its unique identifier.")
    @Parameter(name = "id", description = "ID of the favorite item to remove", required = true, schema = @Schema(type = "integer", format = "int64", example = "123"))
    @ApiResponse(responseCode = "200", description = "Successfully removed the favorite item")
    @ApiResponse(responseCode = "404", description = "Favorite item with the given ID not found")
    void delete(@PathVariable Long id);
}

