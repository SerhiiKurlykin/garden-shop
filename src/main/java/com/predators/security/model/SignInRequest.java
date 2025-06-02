package com.predators.security.model;

import lombok.Builder;

@Builder
public record SignInRequest(String email,
                            String password) {
}
