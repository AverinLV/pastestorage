package com.example.pastestorage.services;

import com.example.pastestorage.models.Paste;
import com.example.pastestorage.repositories.PasteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PasteService {
    private final PasteRepository pasteRepository;

    @Transactional
    public void save(Paste paste) {
        enrichPaste(paste);
        pasteRepository.save(paste);
    }

    private void enrichPaste(Paste paste) {
        paste.setId(UUID.randomUUID().toString());
    }

}
