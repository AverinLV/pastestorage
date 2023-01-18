package com.example.pastestorage.services;

import com.example.pastestorage.models.Paste;
import com.example.pastestorage.repositories.PasteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PasteService {
    private final PasteRepository pasteRepository;

    @Transactional
    public void save(Paste paste) {
        pasteRepository.save(paste);
    }

    public Paste get(UUID id) {
        Optional<Paste> foundPaste = pasteRepository.findById(id);
        return foundPaste.orElse(null);
    }

}
