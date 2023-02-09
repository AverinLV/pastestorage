package com.example.pastestorage.dto.mapper;

import com.example.pastestorage.dto.request.CreatePasteDTO;
import com.example.pastestorage.dto.request.CreateUserDTO;
import com.example.pastestorage.dto.response.UserSignedUpResponseDTO;
import com.example.pastestorage.models.User;
import com.example.pastestorage.security.JWTUtil;
import com.example.pastestorage.types.UserRole;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    private JWTUtil jwtUtil;

    public abstract UserSignedUpResponseDTO toUserSignedUpResponseDTO(String username, String token);
    public abstract User toUser(CreateUserDTO createUserDTO);
    @Mapping(source = "username", target = "token", qualifiedByName = "toToken")
    public abstract UserSignedUpResponseDTO toUserSignedUpResponseDTO(User user);
    @Named("toToken")
    public String usernameToToken(String username) {
        String token = jwtUtil.generateToken(username);
        return token;
    }
    @BeforeMapping
    protected void enrichUserWithRole(CreateUserDTO createUserDTO, @MappingTarget User user) {
        user.setUserRole(UserRole.ROLE_BASIC);
    }

}
