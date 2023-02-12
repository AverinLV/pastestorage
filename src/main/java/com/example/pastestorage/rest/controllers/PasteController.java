package com.example.pastestorage.rest.controllers;

import com.example.pastestorage.dto.mapper.PagePastesMapper;
import com.example.pastestorage.dto.mapper.PasteMapper;
import com.example.pastestorage.dto.request.CreatePasteDTO;
import com.example.pastestorage.dto.response.PagePastesResponseDTO;
import com.example.pastestorage.dto.response.PasteResponseDTO;
import com.example.pastestorage.exceptions.PasteNotFoundException;
import com.example.pastestorage.models.Paste;
import com.example.pastestorage.services.PasteService;
import com.example.pastestorage.validators.AllowedValues;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/pastes")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PasteController {
    private final PasteService pasteService;
    private final PasteMapper pasteMapper;
    private final PagePastesMapper pagePastesMapper;

    @GetMapping()
    public ResponseEntity<PagePastesResponseDTO> getPublicPastes(
            @RequestParam(required = false, defaultValue = "0") @Min(0) int page,
            @RequestParam(required = false, defaultValue = "10") @Min(0) int size,
            @AllowedValues(propName = "orderBy", values = { "createdAt", "expireDate" }) @RequestParam(required = false, defaultValue = "createdAt") String orderBy,
            @AllowedValues(propName = "orderDirection", values = { "asc", "desc" }) @RequestParam(required = false, defaultValue = "desc") String orderDirection,
            @RequestParam(required = false, defaultValue = "1970-01-01T00:00:00.00Z") String minStartDate,
            @RequestParam(required = false, defaultValue = "9999-12-31T00:00:00.00Z") String maxStartDate) {
        Page<Paste> pagePastes = pasteService.getPublic(
                page,
                size,
                orderBy,
                orderDirection,
                Instant.parse(minStartDate),
                Instant.parse(maxStartDate));
        PagePastesResponseDTO pagePastesResponseDTO = pagePastesMapper.toPagePastesResponseDTO(pagePastes);
        return new ResponseEntity<>(pagePastesResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasteResponseDTO> getPaste(@PathVariable("id") String id) {
        UUID uuid = UUID.fromString(id);
        Paste foundPaste = pasteService.get(uuid);
        return new ResponseEntity<>(pasteMapper.toResponseDTO(foundPaste), HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<PasteResponseDTO> createPaste(@Valid @RequestBody CreatePasteDTO createPasteDTO) {
        Paste newPaste = pasteMapper.toPaste(createPasteDTO);
        pasteService.save(newPaste);
        return new ResponseEntity<>(pasteMapper.toResponseDTO(newPaste), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PasteResponseDTO> deletePaste(@PathVariable("id") String id) {
        UUID uuid = UUID.fromString(id);
        Paste deletedPaste = pasteService.delete(uuid);
        return new ResponseEntity<>(pasteMapper.toResponseDTO(deletedPaste), HttpStatus.OK);
    }

}
