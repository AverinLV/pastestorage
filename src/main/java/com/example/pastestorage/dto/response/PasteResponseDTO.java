package com.example.pastestorage.dto.response;

import com.example.pastestorage.types.AccessType;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class PasteResponseDTO {
    String textData;
    String hashLink;
    AccessType accessType;
    Timestamp expireDate;
}
