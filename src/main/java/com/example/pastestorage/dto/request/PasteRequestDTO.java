package com.example.pastestorage.dto.request;

import com.example.pastestorage.types.AccessType;
import lombok.Getter;
import lombok.Setter;

import java.time.temporal.ChronoUnit;

@Getter
@Setter
public class PasteRequestDTO {
    String textData;
    int lifetime;
    ChronoUnit lifetimeType;
    AccessType accessType;
}
