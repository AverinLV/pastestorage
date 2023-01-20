package com.example.pastestorage.dto.response;

import com.example.pastestorage.models.Paste;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagePastesResponseDTO {
    List<Paste> pastes;
    int currentPage;
    int totalItems;
    int totalPages;

}
