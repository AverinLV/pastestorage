package com.example.pastestorage.dto.mapper;

import com.example.pastestorage.dto.response.PagePastesResponseDTO;
import com.example.pastestorage.models.Paste;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(
        componentModel = "spring",
        uses = {PasteMapper.class})
public interface PagePastesMapper {
    @Mapping(target = "pastes", source = "content")
    @Mapping(target = "currentPage", source = "number")
    @Mapping(target = "totalItems", source = "totalElements")
    PagePastesResponseDTO toPagePastesResponseDTO(Page<Paste> pastePage);
}
