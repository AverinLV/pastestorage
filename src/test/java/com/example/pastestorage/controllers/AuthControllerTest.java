package com.example.pastestorage.controllers;

import com.example.pastestorage.dto.mapper.UserMapper;
import com.example.pastestorage.dto.request.CreateUserDTO;
import com.example.pastestorage.dto.request.UserAuthDTO;
import com.example.pastestorage.models.User;
import com.example.pastestorage.services.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends ControllerBaseTest{
    @MockBean
    private AuthenticationService authenticationService;
    @Autowired
    private UserMapper userMapper;

    @Test
    public void shouldPerformSignUp() throws Exception {
        LocalDateTime dateTime = LocalDateTime.now();
        java.sql.Date sqlDate = java.sql.Date.valueOf(dateTime.toLocalDate());
        CreateUserDTO createUserDTO = new CreateUserDTO("username", "password", sqlDate);

        doNothing().when(authenticationService).signUp(any(User.class));

        this.mockMvc.perform(post("/auth/sign_up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(createUserDTO.getUsername()));

    }

    @Test
    public void shouldPerformLogin() throws Exception {
        UserAuthDTO userAuthDTO = new UserAuthDTO("username", "password");
        final String TOKEN = "Token";

        given(authenticationService.loginAndGetToken(any(UserAuthDTO.class))).willReturn(TOKEN);

        this.mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userAuthDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(userAuthDTO.getUsername()))
                .andExpect(jsonPath("$.token").value(TOKEN));

    }

}
