package com.example.pastestorage.security;

import com.example.pastestorage.types.UserRole;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class AuthorityUtil {
    public UserRole getRoleFromContext() {
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities();
        if (authorities.size() != 1) {
            throw new RuntimeException("Found more than 1 authority for user"); //TODO change exception type
        }
        UserRole userRole = UserRole.valueOf(authorities.iterator().next().toString());
        return userRole;
    }

    public int getRolePriority(UserRole role) {
        return UserRole.valueOf(role.name()).ordinal();
    }
}
