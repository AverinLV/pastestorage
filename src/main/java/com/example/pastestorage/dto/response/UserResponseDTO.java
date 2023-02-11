package com.example.pastestorage.dto.response;

import com.example.pastestorage.types.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class UserResponseDTO {
    String username;
    Date birthDate;
    UserRole userRole;
    List<String> unlistedPastesIds;
    List<String> publicPastesIds;

}
