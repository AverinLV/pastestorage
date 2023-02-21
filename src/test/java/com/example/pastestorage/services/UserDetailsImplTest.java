package com.example.pastestorage.services;

import com.example.pastestorage.models.User;
import com.example.pastestorage.repositories.UserRepository;
import com.example.pastestorage.types.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class UserDetailsImplTest extends ServiceBaseTest{
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setUserRole(UserRole.ROLE_BASIC);
    }

    @Test
    @DisplayName("Test loadByUsername")
    void testLoadByUsername() {
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        verify(userRepository, times(1)).findByUsername(user.getUsername());
        assertEquals(userDetails.getPassword(), user.getPassword());
        assertEquals(userDetails.getUsername(), user.getUsername());
    }
}
