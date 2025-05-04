package com.predators.dto.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CategoryResponseDto(Long id,
                                  String name) {
}
