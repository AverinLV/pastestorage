package com.example.pastestorage.dto.request;

import com.example.pastestorage.dto.BaseDTO;
import com.example.pastestorage.types.AccessType;
import com.example.pastestorage.types.LifetimeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
public class CreatePasteDTO extends BaseDTO {
    @NotEmpty()
    String textData;
    @Min(1)
    int lifetime;
    LifetimeType lifetimeType;
    AccessType accessType;
}
