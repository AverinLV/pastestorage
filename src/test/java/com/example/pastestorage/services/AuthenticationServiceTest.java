package com.example.pastestorage.services;

import com.example.pastestorage.dto.request.UserAuthDTO;
import com.example.pastestorage.models.User;
import com.example.pastestorage.repositories.UserRepository;
import com.example.pastestorage.types.UserRole;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest extends ServiceBaseTest{
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationService authenticationService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setUserRole(UserRole.ROLE_BASIC);
    }

    @Test
    @DisplayName("Test signUp")
    void testSignUp() {
        given(userRepository.save(user)).willReturn(user);
        authenticationService.signUp(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Test signUp")
    void testLogin() {
        UserAuthDTO userAuthDTO = new UserAuthDTO(user.getUsername(), user.getPassword());

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willReturn(
                new UsernamePasswordAuthenticationToken(
                        userAuthDTO.getUsername(),
                        userAuthDTO.getPassword()));

        String token = authenticationService.loginAndGetToken(userAuthDTO);

        verify(authenticationManager, times(1)).authenticate(eq(new UsernamePasswordAuthenticationToken(userAuthDTO.getUsername(), userAuthDTO.getPassword())));
        assertNotNull(token);
    }
}
