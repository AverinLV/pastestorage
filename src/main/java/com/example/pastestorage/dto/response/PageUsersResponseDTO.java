package com.example.pastestorage.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageUsersResponseDTO {
    List<UserResponseDTO> users;
    int currentPage;
    int totalItems;
    int totalPages;
}
