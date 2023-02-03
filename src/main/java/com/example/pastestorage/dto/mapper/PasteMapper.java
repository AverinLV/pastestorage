package com.example.pastestorage.dto.mapper;

import com.example.pastestorage.dto.request.CreatePasteDTO;
import com.example.pastestorage.dto.response.PasteResponseDTO;
import com.example.pastestorage.models.Paste;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PasteMapper {
    PasteResponseDTO toResponseDTO(Paste paste);
    @Mapping(target = "expireDate", source = ".", qualifiedByName = "toExpireDate") // source = "." can make use source object by parameter.
    Paste toPaste(CreatePasteDTO createPasteDTO);

    List<PasteResponseDTO> toListResponseDTO(List<Paste> pasteList);

    @Named("toExpireDate")
    default Instant convertToExpireDate(CreatePasteDTO createPasteDTO) {
        int lifetime = createPasteDTO.getLifetime();
        ChronoUnit lifetimeType = ChronoUnit.valueOf(createPasteDTO.getLifetimeType().toString());
        Instant currentTime = Instant.now();
        Instant expireDate = currentTime.plus(lifetime, lifetimeType);
        return expireDate;
    }

}
