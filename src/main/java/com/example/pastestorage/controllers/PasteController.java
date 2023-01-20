package com.example.pastestorage.controllers;

import com.example.pastestorage.dto.mapper.PagePastesMapper;
import com.example.pastestorage.dto.mapper.PasteMapper;
import com.example.pastestorage.dto.request.PasteRequestDTO;
import com.example.pastestorage.dto.response.PagePastesResponseDTO;
import com.example.pastestorage.dto.response.PasteResponseDTO;
import com.example.pastestorage.models.Paste;
import com.example.pastestorage.services.PasteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PasteController {
    private final PasteService pasteService;
    private final PasteMapper pasteMapper;
    private final PagePastesMapper pagePastesMapper;

    @GetMapping("/pastes")
    public ResponseEntity<PagePastesResponseDTO> getPublicPastes(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        Page<Paste> pagePastes = pasteService.getPublic(page, size);
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
    public ResponseEntity<PasteResponseDTO> createPaste(@RequestBody PasteRequestDTO pasteRequestDTO) {
        Paste newPaste = pasteMapper.toPaste(pasteRequestDTO);
        pasteService.save(newPaste);
        return new ResponseEntity<>(pasteMapper.toResponseDTO(newPaste), HttpStatus.CREATED);
    }

}
