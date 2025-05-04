package com.predators.service;

import com.predators.dto.user.UserRequestDto;
import com.predators.entity.ShopUser;
import com.predators.entity.enums.Role;
import com.predators.exception.PermissionDeniedException;
import com.predators.exception.UserAlreadyExistsException;
import com.predators.exception.UserNotFoundException;
import com.predators.repository.UserJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShopUserServiceImpl implements ShopUserService {

    private final UserJpaRepository userRepository;

    public ShopUserServiceImpl(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<ShopUser> getAll() {
        return userRepository.findAll();
    }

    @Override
    public ShopUser create(ShopUser shopUser) {
        Optional<ShopUser> existingUser = userRepository.findByEmail(shopUser.getEmail());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with such email exists");
        }

        return userRepository.save(shopUser);
    }

    @Override
    public ShopUser getById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public void delete(Long id) {
        ShopUser currentUser = getCurrentUser();
        if (currentUser.getId().equals(id) || currentUser.getRole().equals(Role.ROLE_ADMIN)) {
            Optional<ShopUser> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                userRepository.deleteById(id);
            } else {
                throw new UserNotFoundException("Cannot delete user: no user found with id " + id);
            }
        } else {
            throw new PermissionDeniedException("You are not an admin, or this account " + id + " does not belong to you");
        }
    }

    @Override
    public ShopUser getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public ShopUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String email = authentication.getName();
        return getByEmail(email);
    }

    @Override
    @Transactional
    public ShopUser update(UserRequestDto userDto) {
        ShopUser currentUser = getCurrentUser();
        if (userDto.name() != null) {
            currentUser.setName(userDto.name());
        }
        if (userDto.email() != null) {
            if (userRepository.findByEmail(userDto.email()).isPresent()) {
                throw new UserAlreadyExistsException("User with such email exists");
            }
            currentUser.setEmail(userDto.email());
        }
        if (userDto.phone() != null) {
            currentUser.setPhoneNumber(userDto.phone());
        }
        return userRepository.save(currentUser);
    }

}
