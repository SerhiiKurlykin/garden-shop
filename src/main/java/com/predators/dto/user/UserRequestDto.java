package com.predators.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRequestDto(
        @NotBlank(message = "Name must not be empty")
        @Size(min = 2, max = 50, message = "Length of the name should be between 2 and 50 symbols")
        String name,

        @NotBlank(message = "Email must not be empty")
        @Email(message = "Email is not correct")
        String email,

        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Not valid format of phone number")
        String phoneNumber,

        @NotBlank(message = "Password must not be empty")
        @Size(min = 2, max = 50, message = "Length of the password should be between 2 and 50 symbols")
        String password) {
}
