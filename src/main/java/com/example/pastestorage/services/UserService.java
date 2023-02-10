package com.example.pastestorage.services;

import com.example.pastestorage.models.User;
import com.example.pastestorage.repositories.UserRepository;
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
    @Transactional(readOnly = true)
    public boolean isUsernameExist(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent();
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void setRole(long id, UserRole role) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Unable to find user with ID " + id));
        user.setUserRole(role);
        userRepository.save(user);
    }
}
