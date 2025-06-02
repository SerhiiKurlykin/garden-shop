package com.predators.service;

import com.predators.entity.ShopUser;
import com.predators.exception.UserNotFoundException;
import com.predators.repository.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShopShopUserServiceImplTest {

    @Mock
    private UserJpaRepository userRepository;

    @InjectMocks
    private ShopUserServiceImpl userService;

    @BeforeEach
    void setUp() {
        Long testUserId = 1L;
        ShopUser testUser = new ShopUser();
        testUser.setId(testUserId);
    }

    @Test
    public void testGetAll() {
        userService.getAll();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testCreate() {
        userService.create(new ShopUser());
        verify(userRepository, times(1)).save(any(ShopUser.class));
    }

    @Test
    void testGetById() {
        Long id = 1L;
        ShopUser expectedUser = new ShopUser();
        when(userRepository.findById(id)).thenReturn(Optional.of(expectedUser));

        ShopUser actualUser = userService.getById(id);

        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void testGetByIdNotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getById(2L));
    }
}