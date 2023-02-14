package com.example.pastestorage.dto.mapper;

import com.example.pastestorage.dto.request.CreateUserDTO;
import com.example.pastestorage.dto.response.PageUsersResponseDTO;
import com.example.pastestorage.dto.response.UserAuthenticatedResponseDTO;
import com.example.pastestorage.dto.response.UserResponseDTO;
import com.example.pastestorage.models.Paste;
import com.example.pastestorage.models.User;
import com.example.pastestorage.security.JWTUtil;
import com.example.pastestorage.types.AccessType;
import com.example.pastestorage.types.UserRole;
import lombok.Setter;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = AccessType.class)
public abstract class UserMapper {
    @Setter(onMethod_={@Autowired})
    private JWTUtil jwtUtil;
    public abstract UserAuthenticatedResponseDTO toUserSignedUpResponseDTO(String username, String token);
    public abstract User toUser(CreateUserDTO createUserDTO);

    @Mapping(target = "unlistedPastesIds", expression = "java(mapPastes(user.getPastes(), AccessType.UNLISTED))")
    @Mapping(target = "publicPastesIds", expression = "java(mapPastes(user.getPastes(), AccessType.PUBLIC))")
    public abstract UserResponseDTO toUserResponseDTO(User user);

    @Mapping(target = "users", source = "content")
    @Mapping(target = "currentPage", source = "number")
    @Mapping(target = "totalItems", source = "totalElements")
    public abstract PageUsersResponseDTO toPageUsersResponseDTO(Page<User> userPage);

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

    protected List<String> mapPastes(List<Paste> pastes, AccessType accessType) {
        return pastes.stream()
                .filter(paste -> paste.getAccessType().equals(accessType))
                .map(Paste::getId)
                .map(UUID::toString)
                .collect(Collectors.toList());
    }

}
