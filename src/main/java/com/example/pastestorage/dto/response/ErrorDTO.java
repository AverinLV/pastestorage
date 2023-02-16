package com.example.pastestorage.dto.response;

import com.example.pastestorage.dto.BaseDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ErrorDTO extends BaseDTO {
    private final Map<String, List<String>> exceptions;

}
