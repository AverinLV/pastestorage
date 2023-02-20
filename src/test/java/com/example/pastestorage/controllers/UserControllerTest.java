package com.example.pastestorage.controllers;

import com.example.pastestorage.dto.mapper.UserMapper;
import com.example.pastestorage.dto.request.SetUserRoleDTO;
import com.example.pastestorage.models.Paste;
import com.example.pastestorage.models.User;
import com.example.pastestorage.rest.controllers.UserController;
import com.example.pastestorage.services.UserService;
import com.example.pastestorage.types.AccessType;
import com.example.pastestorage.types.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest extends ControllerBaseTest{
    @Autowired
    private UserController userController;
    @Autowired
    private UserMapper userMapper;
    private List<User> userList;
    private List<Paste> pasteList;
    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("testUser");
        user.setUserRole(UserRole.ROLE_BASIC);

        this.userList = new ArrayList<>();
        this.pasteList = new ArrayList<>();
        pasteList.add(new Paste(
                UUID.fromString("91c8f139-845e-428b-9237-a765d41b2a51"),
                "textData",
                Instant.now().plus(1, ChronoUnit.DAYS),
                Instant.now(), AccessType.PUBLIC, user)
        );

        user.setPastes(this.pasteList);
        this.userList.add(user);

        given(authentication.getName()).willReturn(user.getUsername());
        given(securityContext.getAuthentication()).willReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void shouldSetRole() throws Exception {
        User user = userList.get(0);
        SetUserRoleDTO setUserRoleDTO = new SetUserRoleDTO();
        setUserRoleDTO.setRole(UserRole.ROLE_ADMIN);

        doNothing().when(userService).setRole(user.getUsername(), setUserRoleDTO.getRole());

        this.mockMvc.perform(post("/users/{username}/set_role", user.getUsername())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(setUserRoleDTO)))
                .andExpect(status().isOk());
    }
    @Test
    public void shouldGetUser() throws Exception {
        User user = userList.get(0);
        given(this.userService.get(user.getUsername())).willReturn(user);
        this.mockMvc.perform(get("/users/{username}", user.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    public void shouldGetPageUser() throws Exception {
        final int PAGE = 0;
        final int SIZE = 10;
        given(this.userService.getAll(PAGE, SIZE)).willReturn(getPageFromList(
                userList,
                PAGE,
                SIZE,
                null,
                null,
                User.class));
        this.mockMvc.perform(get("/users")
                        .param("page", String.valueOf(PAGE))
                        .param("size", String.valueOf(SIZE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(userList.size()));
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        User user = userList.get(0);
        given(userService.delete(user.getUsername())).willReturn(user);
        this.mockMvc.perform(delete("/users/{username}", user.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

}
