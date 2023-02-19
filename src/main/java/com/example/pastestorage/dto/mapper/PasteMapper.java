package com.example.pastestorage.dto.mapper;

import com.example.pastestorage.dto.request.CreatePasteDTO;
import com.example.pastestorage.dto.request.EditPasteDTO;
import com.example.pastestorage.dto.response.PagePastesResponseDTO;
import com.example.pastestorage.dto.response.PasteResponseDTO;
import com.example.pastestorage.models.Paste;
import com.example.pastestorage.services.UserService;
import lombok.Setter;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class PasteMapper {
    @Setter(onMethod_={@Autowired})
    private UserService userService;

    @Mapping(target = "createdBy", expression = "java(paste.getCreatedBy().getUsername())")
    public abstract PasteResponseDTO toResponseDTO(Paste paste);
    @Mapping(target = "expireDate", source = ".", qualifiedByName = "toExpireDate") // source = "." can make use source object by parameter.
    public abstract Paste toPaste(CreatePasteDTO createPasteDTO);

    public abstract Paste update(EditPasteDTO editPasteDTO, @MappingTarget Paste paste);

    public abstract List<PasteResponseDTO> toListResponseDTO(List<Paste> pasteList);

    @Named("toExpireDate")
    protected Instant convertToExpireDate(CreatePasteDTO createPasteDTO) {
        int lifetime = createPasteDTO.getLifetime();
        ChronoUnit lifetimeType = ChronoUnit.valueOf(createPasteDTO.getLifetimeType().toString());
        Instant currentTime = Instant.now();
        Instant expireDate = currentTime.plus(lifetime, lifetimeType);
        return expireDate;
    }

    @Mapping(target = "pastes", source = "content")
    @Mapping(target = "currentPage", source = "number")
    @Mapping(target = "totalItems", source = "totalElements")
    public abstract PagePastesResponseDTO toPagePastesResponseDTO(Page<Paste> pastePage);

    @BeforeMapping
    protected void enrichPaste(@MappingTarget Paste paste) {
        paste.setCreatedAt(Instant.now());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        paste.setCreatedBy(userService.get(username));
    }

    @BeforeMapping
    protected void enrichPasteUpdate(EditPasteDTO editPasteDTO, @MappingTarget Paste paste) {
        if (Integer.valueOf(editPasteDTO.getLifetime()) != null && editPasteDTO.getLifetimeType() != null) {
            ChronoUnit lifetimeType = ChronoUnit.valueOf(editPasteDTO.getLifetimeType().toString());
            Instant newExpireDate = paste.getExpireDate().plus(editPasteDTO.getLifetime(), lifetimeType);
            paste.setExpireDate(newExpireDate);
        }
    }

}
