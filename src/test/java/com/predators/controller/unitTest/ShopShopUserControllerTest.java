package com.predators.controller.unitTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.predators.controller.ShopUserController;
import com.predators.dto.converter.ShopUserConverter;
import com.predators.dto.user.UserRequestDto;
import com.predators.dto.user.UserResponseDto;
import com.predators.entity.ShopUser;
import com.predators.security.JwtAuthFilter;
import com.predators.security.JwtService;
import com.predators.service.ShopUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ShopUserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class ShopShopUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopUserService shopUserService;

    @MockBean
    private ShopUserConverter shopUserConverter;

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
    public void createUser_ReturnCreated() throws Exception {
        given(shopUserService.create(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void getAllUsers_ReturnsUserList() throws Exception {
        List<ShopUser> userList = Collections.singletonList(user);

        given(shopUserService.getAll()).willReturn(userList);

        given(shopUserConverter.toDto(user)).willReturn(
                new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getPhoneNumber())
        );

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(userList.size()))
                .andExpect(jsonPath("$[0].name").value(user.getName()));
    }

    @Test
    public void getUserById_ReturnsUser() throws Exception {
        Long userId = 1L;

        given(shopUserService.getById(userId)).willReturn(user);
        given(shopUserConverter.toDto(user)).willReturn(
                new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getPhoneNumber())
        );

        mockMvc.perform(get("/v1/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()));
    }

    @Test
    public void deleteUserById_ReturnsOk() throws Exception {
        Long userId = user.getId();
        willDoNothing().given(shopUserService).delete(userId);

        mockMvc.perform(delete("/v1/users/{id}", userId))
                .andExpect(status().isOk());
    }
}