package com.predators.dto.product;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductRequestDto(

        @NotBlank(message = "Name must not be empty")
        @Size(min = 2, max = 50, message = "Length of the name should be between 2 and 50 symbols")
        String name,

        @NotBlank(message = "Description must not be empty")
        @Size(min = 2, max = 500, message = "Length of the description should be between 2 and 500 symbols")
        String description,

        @Digits(message = "The price should be a number", integer = 10, fraction = 2)
        BigDecimal price,

        @Min(1L)
        @Max(999999999L)
        Long categoryId,

        @NotBlank(message = "ImageUrl must not be empty")
        @Size(min = 2, max = 200, message = "Length of the imageUrl should be between 2 and 200 symbols")
        String imageUrl) {
}
