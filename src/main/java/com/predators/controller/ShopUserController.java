package com.predators.controller;

import com.predators.dto.user.ShopUserMapper;
import com.predators.dto.user.UserRequestDto;
import com.predators.dto.user.UserRequestUpdateDto;
import com.predators.dto.user.UserResponseDto;
import com.predators.entity.ShopUser;
import com.predators.security.AuthenticationService;
import com.predators.security.model.SignInRequest;
import com.predators.security.model.SignInResponse;
import com.predators.service.ShopUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class ShopUserController implements ShopUserApi {

    private final ShopUserService shopUserService;

    private final ShopUserMapper shopUserMapper;

    private final AuthenticationService authenticationService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<ShopUser> users = shopUserService.getAll();
        List<UserResponseDto> usersDto = users.stream().map(shopUserMapper::toDto).toList();
        return ResponseEntity.ok(usersDto);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto userDto) {
        ShopUser user = shopUserMapper.toEntity(userDto);
        UserResponseDto dto = shopUserMapper.toDto(shopUserService.create(user));
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Authentication user", description = "Authentication user and get Jwt Token")
    public SignInResponse login(@Valid @RequestBody SignInRequest request) {
        return authenticationService.authenticate(request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDto> getById(@PathVariable(name = "id") Long id) {
        ShopUser user = shopUserService.getById(id);
        UserResponseDto userDto = shopUserMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        shopUserService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserResponseDto> update(@Valid @RequestBody UserRequestUpdateDto userDto) {
        ShopUser update = shopUserService.update(userDto);
        return new ResponseEntity<>(shopUserMapper.toDto(update), HttpStatus.ACCEPTED);
    }
}
