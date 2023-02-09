package com.example.pastestorage.dto.request;

import com.example.pastestorage.validators.UniqueUsername;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Getter
@Setter
public class CreateUserDTO {
    @NotEmpty
    @UniqueUsername
    String username;
    @NotEmpty
    String password;
    Date birthDate;
}
