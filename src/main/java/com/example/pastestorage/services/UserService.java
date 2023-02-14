package com.example.pastestorage.services;

import com.example.pastestorage.exceptions.UnauthorizedActionException;
import com.example.pastestorage.models.User;
import com.example.pastestorage.repositories.UserRepository;
import com.example.pastestorage.security.AuthorityUtil;
import com.example.pastestorage.types.UserRole;
import com.example.pastestorage.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserRepository userRepository;
    private final SessionUtil sessionUtil;
    @Transactional(readOnly = true)
    public boolean isUsernameExist(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent();
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    public void setRole(String username, UserRole role) {
        User userToBeChanged = this.get(username);
        int roleToBeSetPriority = AuthorityUtil.getRolePriority(role);
        int originalRolePriority = AuthorityUtil.getRolePriority(userToBeChanged.getUserRole());
        int requesterRolePriority = AuthorityUtil.getRolePriority(AuthorityUtil.getRoleFromContext());
        if (roleToBeSetPriority > requesterRolePriority) {
            throw new UnauthorizedActionException("Unable to grant role higher than requester role. Requester role: "
                    + AuthorityUtil.getRoleFromContext() + ". Requested role: " + role);
        }
        if (originalRolePriority == requesterRolePriority) {
            throw new UnauthorizedActionException("Unable to change roles with same priority as requester. Requester role: "
                    + AuthorityUtil.getRoleFromContext() + ". Requested role: " + role);
        }
        userToBeChanged.setUserRole(role);
        userRepository.save(userToBeChanged);
    }

    @Transactional(readOnly = true)
    public User get(String username) {
        if (AuthorityUtil.isAllowedAction(username)) {
            sessionUtil.enableExpiredFilter();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            sessionUtil.disableFilter("expiredFilter");
            return user;
        } else {
            throw new UnauthorizedActionException("Only " + UserRole.ROLE_ADMIN + " or higher can access other user's info");
        }
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
    public Page<User> getAll(int page,
                             int size) {
        Pageable paging = PageRequest.of(page, size);
        sessionUtil.enableExpiredFilter();
        Page<User> pageUsers = userRepository.findAll(paging);
        sessionUtil.disableFilter("expiredFilter");
        return pageUsers;
    }

    @Transactional
    public User delete(String username) {
        // Same access rule as in /GET case
        User userToBeDeleted = this.get(username);
        userRepository.delete(userToBeDeleted);
        return userToBeDeleted;
    }

}
