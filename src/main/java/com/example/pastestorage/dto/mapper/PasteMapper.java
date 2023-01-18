package com.example.pastestorage.dto.mapper;

import com.example.pastestorage.dto.request.PasteRequestDTO;
import com.example.pastestorage.dto.response.PasteResponseDTO;
import com.example.pastestorage.models.Paste;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring")
public interface PasteMapper {
    PasteResponseDTO toResponseDTO(Paste paste);
    @Mapping(target = "expireDate", source = ".", qualifiedByName = "toExpireDate") // source = "." can make use source object by parameter.
    Paste toPaste(PasteRequestDTO pasteRequestDTO);

    @Named("toExpireDate")
    default Instant convertToExpireDate(PasteRequestDTO pasteRequestDTO) {
        int lifetime = pasteRequestDTO.getLifetime();
        ChronoUnit lifetimeType = ChronoUnit.valueOf(pasteRequestDTO.getLifetimeType().toString());
        Instant currentTime = Instant.now();
        Instant expireDate = currentTime.plus(lifetime, lifetimeType);
        return expireDate;
    }
}
