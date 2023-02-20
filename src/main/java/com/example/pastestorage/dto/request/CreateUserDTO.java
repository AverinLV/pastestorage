package com.example.pastestorage.dto.request;

import com.example.pastestorage.dto.BaseDTO;
import com.example.pastestorage.validators.UniqueUsername;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class CreateUserDTO extends BaseDTO {
    @NotEmpty
    @UniqueUsername
    String username;
    @NotEmpty
    String password;
    Date birthDate;
}
