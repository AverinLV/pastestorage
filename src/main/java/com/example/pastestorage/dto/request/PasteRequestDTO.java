package com.example.pastestorage.dto.request;

import com.example.pastestorage.types.AccessType;
import com.example.pastestorage.types.LifetimeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasteRequestDTO {
    String textData;
    int lifetime;
    LifetimeType lifetimeType;
    AccessType accessType;
}
