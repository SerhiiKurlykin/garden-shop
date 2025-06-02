package com.predators.controller;

import com.predators.dto.product.ProductFilterDto;
import com.predators.dto.product.ProductRequestDto;
import com.predators.dto.product.ProductResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Tag(name = "Product Management", description = "Operations for managing products")
public interface ProductApi {

    @Operation(summary = "Get paginated products",
            description = "Retrieves a paginated list of products with filtering and sorting options")
    @Parameters({
            @Parameter(name = "page", description = "Page number (0-based)", example = "0", schema = @Schema(type = "integer", defaultValue = "0")),
            @Parameter(name = "size", description = "Number of items per page", example = "10", schema = @Schema(type = "integer", defaultValue = "10")),
            @Parameter(name = "sort", description = "Sorting criteria in format: property,direction. Multiple sort params allowed",
                    example = "name,asc", schema = @Schema(type = "string", defaultValue = "name,asc"))})
    @ApiResponse(responseCode = "200", description = "Successfully retrieved products",
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid filter or sort parameters")
    ResponseEntity<Page<ProductResponseDto>> getAll(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "name;asc", name = "sort") String[] sort,
            @ModelAttribute ProductFilterDto filter) throws BadRequestException;

    @Operation(summary = "Create new product (Admin only)",
            description = "Creates a new product. Requires ADMIN role.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product details", required = true,
            content = @Content(schema = @Schema(implementation = ProductRequestDto.class)))
    @ApiResponse(responseCode = "201", description = "Product created successfully",
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid product data")
    @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    ResponseEntity<ProductResponseDto> create(@RequestBody ProductRequestDto productDto);

    @Operation(summary = "Get product by ID", description = "Retrieves a specific product by its unique identifier")
    @Parameter(name = "id", description = "ID of the product to retrieve", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the product",
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Product not found")
    ResponseEntity<ProductResponseDto> getById(@PathVariable Long id);

    @Operation(summary = "Get product by ID", description = "Retrieves a specific product by its unique identifier")
    @Parameter(name = "id", description = "ID of the product to retrieve", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the product",
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Product not found")
    ResponseEntity<Void> delete(@PathVariable Long id);

    @Operation(summary = "Update product", description = "Updates an existing product with new data")
    @Parameter(name = "id", description = "ID of the product to update", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated product details", required = true,
            content = @Content(schema = @Schema(implementation = ProductRequestDto.class)))
    @ApiResponse(responseCode = "201", description = "Product updated successfully",
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid product data")
    @ApiResponse(responseCode = "404", description = "Product not found")
    ResponseEntity<ProductResponseDto> update(@PathVariable(name = "id") Long id,
                                              @RequestBody ProductRequestDto productDto);

    @Operation(summary = "Set product discount (Admin only)", description = "Sets discount price for a product. Requires ADMIN role.")
    @Parameter(name = "id", description = "ID of the product to update", required = true,
            schema = @Schema(type = "integer", format = "int64", example = "1"))
    @Parameter(name = "discount", description = "Discount amount to set", required = true,
            schema = @Schema(type = "number", format = "decimal", example = "15.99"))
    @ApiResponse(responseCode = "202", description = "Discount set successfully",
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class)))
    @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role")
    @ApiResponse(responseCode = "404", description = "Product not found")
    ResponseEntity<ProductResponseDto> setDiscount(@PathVariable Long id, @RequestParam BigDecimal discount);

    @Operation(summary = "Get product of the day", description = "Retrieves the current product of the day")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved product of the day",
            content = @Content(schema = @Schema(implementation = ProductResponseDto.class)))
    @ApiResponse(responseCode = "404", description = "Product of the day not set")
    ResponseEntity<ProductResponseDto> getDayProduct();
}
