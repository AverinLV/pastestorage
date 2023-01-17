package com.example.pastestorage.controllers;

import com.example.pastestorage.dto.request.PasteRequestDTO;
import com.example.pastestorage.dto.response.PasteResponseDTO;
import com.example.pastestorage.models.Paste;
import com.example.pastestorage.services.PasteService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PasteController {
    private final PasteService pasteService;
    private final ModelMapper modelMapper;
    @GetMapping("/{id}")
    public PasteResponseDTO getPaste(@PathVariable("hash") String id) {
        return null;
    }
    @PostMapping()
    public ResponseEntity<HttpStatus> createPaste(@RequestBody PasteRequestDTO pasteRequestDTO) {
        Paste newPaste = convertToPaste(pasteRequestDTO);
        pasteService.save(newPaste);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    private Paste convertToPaste(PasteRequestDTO pasteRequestDTO) {
        return modelMapper.map(pasteRequestDTO, Paste.class);
    }
}
