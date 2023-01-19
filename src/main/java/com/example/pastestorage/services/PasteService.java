package com.example.pastestorage.services;

import com.example.pastestorage.models.Paste;
import com.example.pastestorage.repositories.PasteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PasteService {
    private final PasteRepository pasteRepository;

    @Transactional
    public void save(Paste paste) {
        enrichPaste(paste);
        pasteRepository.save(paste);
    }
    @Transactional(readOnly = true)
    public Paste get(UUID id) {
        Optional<Paste> foundPaste = pasteRepository.findByIdAndExpireDateGreaterThan(id, Instant.now());
        return foundPaste.orElse(null);
    }

    public void enrichPaste(Paste paste) {
        paste.setCreatedAt(Instant.now());
    }

}
