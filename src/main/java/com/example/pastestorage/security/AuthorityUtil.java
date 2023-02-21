package com.example.pastestorage.security;

import com.example.pastestorage.types.UserRole;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class AuthorityUtil {
    public static UserRole getRoleFromContext() {
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities();
        UserRole userRole = UserRole.valueOf(authorities.iterator().next().toString());
        return userRole;
    }

    public static int getRolePriority(UserRole role) {
        return UserRole.valueOf(role.name()).ordinal();
    }

    public static boolean isAllowedAction(String entityUsername) {
        UserRole requesterRole = AuthorityUtil.getRoleFromContext();
        String requesterUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (entityUsername.equals(requesterUsername) || AuthorityUtil.getRolePriority(requesterRole) >= AuthorityUtil.getRolePriority(UserRole.ROLE_ADMIN)) {
            return true;
        } else {
            return false;
        }
    }
}
