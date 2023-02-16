package com.example.pastestorage.dto.response;

import com.example.pastestorage.dto.BaseDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MessageDTO extends BaseDTO {
    private final String message;

}