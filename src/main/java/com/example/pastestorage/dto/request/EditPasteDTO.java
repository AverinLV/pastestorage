package com.example.pastestorage.dto.request;

import com.example.pastestorage.dto.BaseDTO;
import com.example.pastestorage.types.AccessType;
import com.example.pastestorage.types.LifetimeType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class EditPasteDTO extends BaseDTO {
    @NotEmpty()
    String textData;
    int lifetime;
    LifetimeType lifetimeType;
    AccessType accessType;

}
