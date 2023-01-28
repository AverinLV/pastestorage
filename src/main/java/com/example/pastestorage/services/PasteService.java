package com.example.pastestorage.services;

import com.example.pastestorage.models.Paste;
import com.example.pastestorage.repositories.PasteRepository;
import com.example.pastestorage.types.AccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Optional<Paste> get(UUID id) {
        Optional<Paste> foundPaste = pasteRepository.findByIdAndExpireDateGreaterThan(id, Instant.now());
        return foundPaste;
    }

    @Transactional(readOnly = true)
    public Page<Paste> getPublic(int page, int size, String orderBy, String orderDirection) {
        Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(orderDirection), orderBy));
        Page<Paste> pagePastes = pasteRepository.findByAccessTypeAndExpireDateGreaterThan(
                AccessType.PUBLIC,
                Instant.now(),
                paging);
        return pagePastes;
    }

    private void enrichPaste(Paste paste) {
        paste.setCreatedAt(Instant.now());
    }

}
