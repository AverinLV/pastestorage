package com.example.pastestorage.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagePastesResponseDTO {
    List<PasteResponseDTO> pastes;
    int currentPage;
    int totalItems;
    int totalPages;

}
