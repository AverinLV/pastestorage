package com.example.pastestorage.dto.mapper;

import com.example.pastestorage.dto.request.CreatePasteDTO;
import com.example.pastestorage.dto.request.CreateUserDTO;
import com.example.pastestorage.dto.response.PasteResponseDTO;
import com.example.pastestorage.models.Paste;
import com.example.pastestorage.models.User;
import com.example.pastestorage.services.UserService;
import com.example.pastestorage.types.UserRole;
import lombok.Setter;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PasteMapper {
    @Setter(onMethod_={@Autowired})
    private UserService userService;
    public abstract PasteResponseDTO toResponseDTO(Paste paste);
    @Mapping(target = "expireDate", source = ".", qualifiedByName = "toExpireDate") // source = "." can make use source object by parameter.
    public abstract Paste toPaste(CreatePasteDTO createPasteDTO);

    public abstract List<PasteResponseDTO> toListResponseDTO(List<Paste> pasteList);

    @Named("toExpireDate")
    protected Instant convertToExpireDate(CreatePasteDTO createPasteDTO) {
        int lifetime = createPasteDTO.getLifetime();
        ChronoUnit lifetimeType = ChronoUnit.valueOf(createPasteDTO.getLifetimeType().toString());
        Instant currentTime = Instant.now();
        Instant expireDate = currentTime.plus(lifetime, lifetimeType);
        return expireDate;
    }

    @BeforeMapping
    protected void enrichPaste(@MappingTarget Paste paste) {
        paste.setCreatedAt(Instant.now());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        paste.setCreatedBy(userService.get(username));
    }

}
