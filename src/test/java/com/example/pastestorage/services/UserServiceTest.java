package com.example.pastestorage.services;

import com.example.pastestorage.models.User;
import com.example.pastestorage.repositories.UserRepository;
import com.example.pastestorage.security.AuthorityUtil;
import com.example.pastestorage.types.UserRole;
import com.example.pastestorage.util.SessionUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


class UserServiceTest extends ServiceBaseTest{
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testUser");
        user.setUserRole(UserRole.ROLE_BASIC);
    }

    @Test
    @DisplayName("Test isUsernameExist with existing username")
    void testIsUsernameExistWithExistingUsername() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        boolean result = userService.isUsernameExist(user.getUsername());
        assertTrue(result);
    }

    @Test
    @DisplayName("Test isUsernameExist with non-existing username")
    void testIsUsernameExistWithNonExistingUsername() {
        String username = "testuser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        boolean result = userService.isUsernameExist(username);
        assertFalse(result);
    }

    @Test
    @DisplayName("Test getUser")
    void testGetUser() {

        MockedStatic<AuthorityUtil> utilities = Mockito.mockStatic(AuthorityUtil.class);
        utilities.when(() -> AuthorityUtil.isAllowedAction(user.getUsername()))
                .thenReturn(true);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        User gotUser = userService.get(user.getUsername());

        assertEquals(gotUser.getUsername(), user.getUsername());
        utilities.close();
    }

    @Test
    @DisplayName("Test setRole")
    @WithMockUser(roles = {"ADMIN"})
    void testSetRole() {

        MockedStatic<AuthorityUtil> utilities = Mockito.mockStatic(AuthorityUtil.class);
        utilities.when(() -> AuthorityUtil.getRoleFromContext())
                .thenReturn(UserRole.ROLE_ADMIN);
        utilities.when(() -> AuthorityUtil.isAllowedAction(user.getUsername()))
                .thenReturn(true);
        utilities.when(() -> AuthorityUtil.getRolePriority(UserRole.ROLE_ADMIN))
                .thenReturn(1);
        utilities.when(() -> AuthorityUtil.getRolePriority(UserRole.ROLE_BASIC))
                .thenReturn(0);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        userService.setRole(user.getUsername(), UserRole.ROLE_ADMIN);

        verify(userRepository, times(1)).save(user);
        utilities.close();
    }

    @Test
    @DisplayName("Test getAll")
    @WithMockUser(roles = {"ADMIN"})
    void testGetAll() {
        final int PAGE = 0;
        final int SIZE = 10;
        Pageable paging = PageRequest.of(PAGE, SIZE);
        when(userRepository.findAll(paging)).thenReturn(
                getPageFromList(
                        Collections.singletonList(user),
                        PAGE,
                        SIZE,
                        null,
                        null,
                        User.class));
        Page<User> pageUser = userService.getAll(PAGE, SIZE);
        assertEquals(pageUser.getTotalElements(), 1);
    }

    @Test
    @DisplayName("Test delete")
    void testDelete() {
        MockedStatic<AuthorityUtil> utilities = Mockito.mockStatic(AuthorityUtil.class);
        utilities.when(() -> AuthorityUtil.isAllowedAction(user.getUsername()))
                .thenReturn(true);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        User userToDelete = userService.delete(user.getUsername());
        assertEquals(userToDelete.getUsername(), user.getUsername());
        verify(userRepository, times(1)).delete(user);
        utilities.close();
    }

}
