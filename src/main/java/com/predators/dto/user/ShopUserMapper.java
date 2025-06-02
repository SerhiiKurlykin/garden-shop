package com.predators.dto.user;

import com.predators.entity.ShopUser;
import com.predators.service.ShopUserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class ShopUserMapper {

    @Autowired
    protected ShopUserService shopUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mapping(target = "role", constant = "ROLE_CLIENT")
    @Mapping(target = "passwordHash", source = "password", qualifiedByName = "hashPassword")
    public abstract ShopUser toEntity(UserRequestDto userRequestDto);

    public abstract UserResponseDto toDto(ShopUser user);

    @Named("hashPassword")
    protected String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
