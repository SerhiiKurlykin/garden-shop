package com.predators.dto.converter;

import com.predators.dto.user.UserRequestDto;
import com.predators.dto.user.UserResponseDto;
import com.predators.entity.ShopUser;
import com.predators.entity.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ShopUserConverter implements Converter<UserRequestDto, UserResponseDto, ShopUser> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ShopUser toEntity(UserRequestDto userDto) {

        return ShopUser.builder()
                .name(userDto.name())
                .email(userDto.email())
                .phoneNumber(userDto.phone())
                .role(Role.ROLE_CLIENT)
                .passwordHash(passwordEncoder.encode(userDto.password()))
                .build();
    }

    public UserResponseDto toDto(ShopUser user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
