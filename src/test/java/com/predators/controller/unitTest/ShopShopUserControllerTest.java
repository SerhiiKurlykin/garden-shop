package com.predators.controller.unitTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.predators.controller.ShopUserController;
import com.predators.dto.user.ShopUserMapper;
import com.predators.dto.user.UserRequestDto;
import com.predators.entity.ShopUser;
import com.predators.security.JwtAuthFilter;
import com.predators.security.JwtService;
import com.predators.service.ShopUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ShopUserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ShopShopUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopUserService shopUserService;

    @MockBean
    private ShopUserMapper userMapper;

    @MockBean
    private ShopUserController shopUserController;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    private UserRequestDto userRequestDto;

    private ShopUser user;

    @BeforeEach
    public void init() {
        userRequestDto = new UserRequestDto(
                "Test User", "testuser@mail.com", "+49-151-768-13-91", "password"
        );
        user = ShopUser.builder()
                .id(1L)
                .name("Test User")
                .email("testuser@mail.com")
                .phoneNumber("+49-151-768-13-91")
                .passwordHash("password")
                .build();
    }

    @Test
    public void deleteUserById_ReturnsOk() throws Exception {
        Long userId = user.getId();
        willDoNothing().given(shopUserService).delete(userId);

        mockMvc.perform(delete("/v1/users/{id}", userId))
                .andExpect(status().isOk());
    }
}