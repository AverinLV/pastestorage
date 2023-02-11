package com.example.pastestorage.dto.mapper;

import com.example.pastestorage.dto.request.CreateUserDTO;
import com.example.pastestorage.dto.response.UserAuthenticatedResponseDTO;
import com.example.pastestorage.models.User;
import com.example.pastestorage.security.JWTUtil;
import com.example.pastestorage.types.UserRole;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    private JWTUtil jwtUtil;

    public abstract UserAuthenticatedResponseDTO toUserSignedUpResponseDTO(String username, String token);
    public abstract User toUser(CreateUserDTO createUserDTO);
    @Mapping(source = "username", target = "token", qualifiedByName = "toToken")
    public abstract UserAuthenticatedResponseDTO toUserSignedUpResponseDTO(User user);
    @Named("toToken")
    protected String usernameToToken(String username) {
        String token = jwtUtil.generateToken(username);
        return token;
    }
    @BeforeMapping
    protected void enrichUserWithRole(@MappingTarget User user) {
        user.setUserRole(UserRole.ROLE_BASIC);
    }

}
