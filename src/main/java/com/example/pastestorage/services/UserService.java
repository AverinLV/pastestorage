package com.example.pastestorage.services;

import com.example.pastestorage.exceptions.UnauthorizedActionException;
import com.example.pastestorage.models.User;
import com.example.pastestorage.repositories.UserRepository;
import com.example.pastestorage.security.AuthorityUtil;
import com.example.pastestorage.types.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityUtil authorityUtil;
    @Transactional(readOnly = true)
    public boolean isUsernameExist(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent();
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    public void setRole(long id, UserRole role) {
        User userToBeChanged = userRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Unable to find user with ID " + id));
        int roleToBeSetPriority = authorityUtil.getRolePriority(role);
        int originalRolePriority = authorityUtil.getRolePriority(userToBeChanged.getUserRole());
        int requesterRolePriority = authorityUtil.getRolePriority(authorityUtil.getRoleFromContext());
        if (roleToBeSetPriority > requesterRolePriority) {
            throw new UnauthorizedActionException("Unable to grant role higher than requester role. Requester role: "
                    + authorityUtil.getRoleFromContext() + ". Requested role: " + role);
        }
        if (originalRolePriority == requesterRolePriority) {
            throw new UnauthorizedActionException("Unable to change roles with same priority as requester. Requester role: "
                    + authorityUtil.getRoleFromContext() + ". Requested role: " + role);
        }
        userToBeChanged.setUserRole(role);
        userRepository.save(userToBeChanged);
    }
}
