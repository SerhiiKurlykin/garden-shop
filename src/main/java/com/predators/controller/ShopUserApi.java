package com.predators.controller;

import com.predators.dto.user.UserRequestDto;
import com.predators.dto.user.UserRequestUpdateDto;
import com.predators.dto.user.UserResponseDto;
import com.predators.security.model.SignInRequest;
import com.predators.security.model.SignInResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "User", description = "Operations related to users")
public interface ShopUserApi {
    @Operation(summary = "Get all users , (Admin only)", description = "Returns all users." )
    ResponseEntity<List<UserResponseDto>> getAll();

    @Operation(summary = "Create user", description = "Returns new user.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User to be created",
            required = true, content = @Content(schema = @Schema(implementation = UserRequestDto.class)))
    ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto userDto);

    @Operation(summary = "Authentication user", description = "Authentication user and get Jwt Token")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Get Jwt Token",
            required = true, content = @Content(schema = @Schema(implementation = SignInRequest.class)))
    SignInResponse login(@Valid @RequestBody SignInRequest request);

    @Operation(summary = "Get user by Id, (Admin only)", description = "Get user by Id")
    @Parameter(name = "id", description = "Unique identifier of the user",
            required = true, schema = @Schema(type = "integer", format = "int64", example = "1"))
    ResponseEntity<UserResponseDto> getById(@PathVariable(name = "id") Long id);

    @Operation(summary = "Delete user by Id", description = "Delete user by Id")
    @Parameter(name = "id", description = "Unique identifier of the user",
            required = true, schema = @Schema(type = "integer", format = "int64", example = "123"))
    ResponseEntity<Void> delete(@PathVariable(name = "id") Long id);


    @Operation(summary = "Update current user", description = "Update current user and returns user")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Any from three fields",
            required = true, content = @Content(schema = @Schema(implementation = UserRequestUpdateDto.class)))
    ResponseEntity<UserResponseDto> update(@RequestBody UserRequestUpdateDto userDto);
}
