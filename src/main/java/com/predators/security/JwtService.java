package com.predators.security;

import com.predators.entity.ShopUser;

public interface JwtService {

    String generateToken(ShopUser user);

    String extractUserName(String token);

    boolean isTokenValid(String token);
}
