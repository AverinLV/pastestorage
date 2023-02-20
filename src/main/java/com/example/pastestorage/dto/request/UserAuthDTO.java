package com.example.pastestorage.dto.request;

import com.example.pastestorage.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
public class UserAuthDTO extends BaseDTO {
    @NotEmpty
    String username;

    @NotEmpty
    String password;
}
