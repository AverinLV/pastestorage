package com.example.pastestorage.dto.request;

import com.example.pastestorage.types.AccessType;
import com.example.pastestorage.types.LifetimeType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
public class PasteRequestDTO {
    @NotEmpty()
    String textData;
    @Min(1)
    int lifetime;
    LifetimeType lifetimeType;
    AccessType accessType;
}
