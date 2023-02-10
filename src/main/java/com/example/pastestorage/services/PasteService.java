package com.example.pastestorage.services;

import com.example.pastestorage.exceptions.PasteNotFoundException;
import com.example.pastestorage.models.Paste;
import com.example.pastestorage.repositories.PasteRepository;
import com.example.pastestorage.types.AccessType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PasteService {
    private final PasteRepository pasteRepository;

    @Transactional
    public void save(Paste paste) {
        pasteRepository.save(paste);
    }
    @Transactional(readOnly = true)
    public Paste get(UUID id) {
        Paste foundPaste = pasteRepository
                .findByIdAndExpireDateGreaterThan(id, Instant.now())
                .orElseThrow(() -> new PasteNotFoundException("Paste with id " + id + " is not found"));
        return foundPaste;
    }

    @Transactional(readOnly = true)
    public Page<Paste> getPublic(int page,
                                 int size,
                                 String orderBy,
                                 String orderDirection,
                                 Instant minStartDate,
                                 Instant maxStartDate) {
        Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(orderDirection), orderBy));
        Page<Paste> pagePastes = pasteRepository.findNotExpired(
                AccessType.PUBLIC,
                Instant.now(),
                minStartDate,
                maxStartDate,
                paging);
        return pagePastes;
    }

    @Transactional
    @Scheduled(cron = "${cron.clean_database_time_expression}") // Defined in properties file. Cron format
    public void deleteExpired() {
        pasteRepository.deleteExpired(Instant.now());
    }

}
