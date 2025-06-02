package com.predators.dto.category;

import groovy.transform.builder.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Builder
public record CategoryRequestDto(

        @NotBlank(message = "Name must not be empty")
        @Size(min = 2, max = 50, message = "Length of the name should be between 2 and 50 symbols")
        String name) {
}
