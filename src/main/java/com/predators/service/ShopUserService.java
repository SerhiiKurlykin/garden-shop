package com.predators.service;

import com.predators.dto.user.UserRequestDto;
import com.predators.entity.ShopUser;

import java.util.List;

public interface ShopUserService {

    List<ShopUser> getAll();

    ShopUser create(ShopUser user);

    ShopUser getById(Long id);

    void delete(Long id);

    ShopUser getByEmail(String email);

    ShopUser getCurrentUser();

    ShopUser update(UserRequestDto userDto);
}
