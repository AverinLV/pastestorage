package com.example.pastestorage.dto.response;

import com.example.pastestorage.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageUsersResponseDTO extends BaseDTO {
    List<UserResponseDTO> users;
    int currentPage;
    int totalItems;
    int totalPages;
}
