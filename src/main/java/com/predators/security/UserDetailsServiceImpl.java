package com.predators.security;

import com.predators.entity.ShopUser;
import com.predators.service.ShopUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ShopUserService shopUserService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ShopUser byEmail = shopUserService.getByEmail(email);
        return new User(byEmail.getEmail(), byEmail.getPasswordHash(),
                List.of(new SimpleGrantedAuthority(byEmail.getRole().name())));
    }
}

