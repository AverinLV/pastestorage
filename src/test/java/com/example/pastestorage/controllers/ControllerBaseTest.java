package com.example.pastestorage.controllers;

import com.example.pastestorage.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public abstract class ControllerBaseTest {
    @MockBean
    protected SecurityContextHolder securityContextHolder;
    @MockBean
    protected Authentication authentication;
    @MockBean
    protected SecurityContext securityContext;
    @MockBean
    protected UserService userService;
    @Autowired
    protected MockMvc mockMvc;

}
