package com.example.pastestorage.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthenticatedResponseDTO {
    private String username;
    private String token;
}
