package com.predators.controller;

import com.predators.dto.converter.ShopUserConverter;
import com.predators.dto.user.UserRequestDto;
import com.predators.dto.user.UserResponseDto;
import com.predators.entity.ShopUser;
import com.predators.security.AuthenticationService;
import com.predators.security.model.SignInRequest;
import com.predators.security.model.SignInResponse;
import com.predators.service.ShopUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Tag(name = "User", description = "Operations related to users")
public class ShopUserController {

    private final ShopUserService shopUserService;

    private final ShopUserConverter shopUserConverter;

    private final AuthenticationService authenticationService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get all users", description = "Returns all users.")
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<ShopUser> users = shopUserService.getAll();
        List<UserResponseDto> usersDto = users.stream().map(shopUserConverter::toDto).toList();
        return ResponseEntity.ok(usersDto);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create user", description = "Returns new user.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User to be created",
            required = true, content = @Content(schema = @Schema(implementation = UserRequestDto.class)))
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto userDto) {
        ShopUser user = shopUserConverter.toEntity(userDto);
        UserResponseDto dto = shopUserConverter.toDto(shopUserService.create(user));
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Authentication user", description = "Authentication user and get Jwt Token")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Get Jwt Token",
            required = true, content = @Content(schema = @Schema(implementation = SignInRequest.class)))
    public SignInResponse login(@RequestBody SignInRequest request) {
        return authenticationService.authenticate(request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get user by Id", description = "Get user by Id")
    @Parameter(name = "id", description = "Unique identifier of the user",
            required = true, schema = @Schema(type = "integer", format = "int64", example = "1"))
    public ResponseEntity<UserResponseDto> getById(@PathVariable(name = "id") Long id) {
        ShopUser user = shopUserService.getById(id);
        UserResponseDto userDto = shopUserConverter.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by Id", description = "Delete user by Id")
    @Parameter(name = "id", description = "Unique identifier of the user",
            required = true, schema = @Schema(type = "integer", format = "int64", example = "123"))
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        shopUserService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    @Operation(summary = "Update current user", description = "Update current user and returns user")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Any from three fields",
            required = true, content = @Content(schema = @Schema(implementation = UserRequestDto.class)))
    public ResponseEntity<UserResponseDto> update(@RequestBody UserRequestDto userDto) {
        ShopUser update = shopUserService.update(userDto);
        return new ResponseEntity<>(shopUserConverter.toDto(update), HttpStatus.ACCEPTED);
    }
}
