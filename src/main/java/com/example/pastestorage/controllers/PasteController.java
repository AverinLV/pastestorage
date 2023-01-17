package com.example.pastestorage.controllers;

import com.example.pastestorage.dto.request.PasteRequestDTO;
import com.example.pastestorage.dto.response.PasteResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class PasteController {
    @GetMapping("/{hash}")
    public PasteResponseDTO getPaste(@PathVariable("hash") String hash) {
        return null;
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> createPaste(@RequestBody PasteRequestDTO pasteRequestDTO) {
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
