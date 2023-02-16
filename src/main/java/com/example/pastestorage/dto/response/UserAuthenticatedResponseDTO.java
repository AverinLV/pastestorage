package com.example.pastestorage.dto.response;

import com.example.pastestorage.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthenticatedResponseDTO extends BaseDTO {
    private String username;
    private String token;
}
