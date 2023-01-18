package com.example.pastestorage.controllers;

import com.example.pastestorage.dto.mapper.PasteMapper;
import com.example.pastestorage.dto.request.PasteRequestDTO;
import com.example.pastestorage.dto.response.PasteResponseDTO;
import com.example.pastestorage.models.Paste;
import com.example.pastestorage.services.PasteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/{id}")
    public PasteResponseDTO getPaste(@PathVariable("id") String id) {
        UUID uuid = UUID.fromString(id);
        Paste foundPaste = pasteService.get(uuid);
        PasteResponseDTO pasteResponseDTO = pasteMapper.toResponseDTO(foundPaste);
        return pasteResponseDTO;
    }
    @PostMapping()
    public ResponseEntity<HttpStatus> createPaste(@RequestBody PasteRequestDTO pasteRequestDTO) {
        Paste newPaste = pasteMapper.toPaste(pasteRequestDTO);
        pasteService.save(newPaste);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
