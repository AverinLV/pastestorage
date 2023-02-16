package com.example.pastestorage.dto.response;

import com.example.pastestorage.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagePastesResponseDTO extends BaseDTO {
    List<PasteResponseDTO> pastes;
    int currentPage;
    int totalItems;
    int totalPages;

}
