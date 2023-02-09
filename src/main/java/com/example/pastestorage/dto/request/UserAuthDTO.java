package com.example.pastestorage.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserAuthDTO {
    @NotEmpty
    String username;

    @NotEmpty
    String password;
}
