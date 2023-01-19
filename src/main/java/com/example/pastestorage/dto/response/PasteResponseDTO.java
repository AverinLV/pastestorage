package com.example.pastestorage.dto.response;

import com.example.pastestorage.types.AccessType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class PasteResponseDTO {
    String textData;
    String id;
    AccessType accessType;
    Instant expireDate;
    Instant createdAt;

}
