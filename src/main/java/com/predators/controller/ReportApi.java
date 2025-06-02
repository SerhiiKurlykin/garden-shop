package com.predators.controller;

import com.predators.dto.product.ProductCountDto;
import com.predators.dto.product.ProductResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Tag(name = "Report Generation", description = "Operations for generating various reports")
public interface ReportApi {

    @Operation(summary = "Get top products by order status (Admin only)",
            description = "Retrieves a list of top products based on the provided order status. Requires ADMIN role.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of top products",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class))))
    @ApiResponse(responseCode = "400", description = "Invalid order status provided",
            content = @Content(schema = @Schema(type = "string", example = "Invalid order status: UNKNOWN_STATUS")))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role",
            content = @Content(schema = @Schema(type = "string", example = "Access Denied")))
    ResponseEntity<List<ProductCountDto>> getTopProduct(@RequestParam String status);

    @Operation(summary = "Get products waiting for payment for more than N days (Admin only)",
            description = "Retrieves a list of products that have been waiting for payment for more than the specified number of days. Requires ADMIN role.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of products waiting for payment",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponseDto.class))))
    @ApiResponse(responseCode = "400", description = "Invalid number of days provided",
            content = @Content(schema = @Schema(type = "string", example = "Days must be a positive integer")))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role",
            content = @Content(schema = @Schema(type = "string", example = "Access Denied")))
    ResponseEntity<Set<ProductResponseDto>> waitingPaymentMoreNDays(@RequestParam Long days);

    @Operation(summary = "Get profit (Admin only)",
            description = "Retrieves the total profit, optionally filtered by day, month, or year. Requires ADMIN role.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the profit",
            content = @Content(schema = @Schema(type = "number", format = "float", example = "15000.75")))
    @ApiResponse(responseCode = "400", description = "Invalid date parameters provided",
            content = @Content(schema = @Schema(type = "string", example = "Invalid day or month value")))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden - requires ADMIN role",
            content = @Content(schema = @Schema(type = "string", example = "Access Denied")))
    ResponseEntity<BigDecimal> getProfit(@RequestParam(name = "day", required = false) Long day,
                                         @RequestParam(name = "month", required = false) Long month,
                                         @RequestParam(name = "year", required = false) Long year);
}
