package com.example.pastestorage.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Getter
@Setter
public class EditPasteDTO {
    @NotEmpty()
    String textData;

}
