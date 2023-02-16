package com.example.pastestorage.dto.request;

import com.example.pastestorage.dto.BaseDTO;
import com.example.pastestorage.types.UserRole;
import lombok.Getter;

@Getter
public class SetUserRoleDTO extends BaseDTO {
    UserRole role;
}
