package com.predators.security;

import com.predators.entity.ShopUser;
import com.predators.security.model.SignInRequest;
import com.predators.security.model.SignInResponse;
import com.predators.service.ShopUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final ShopUserService shopUserService;

    private final JwtService jwtService;

    @Override
    public SignInResponse authenticate(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        ShopUser user = shopUserService.getByEmail(request.email());
        String token = jwtService.generateToken(user);
        return new SignInResponse(token);
    }
}

