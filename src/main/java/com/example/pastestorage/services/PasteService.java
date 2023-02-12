package com.example.pastestorage.services;

import com.example.pastestorage.exceptions.PasteNotFoundException;
import com.example.pastestorage.exceptions.UnauthorizedActionException;
import com.example.pastestorage.models.Paste;
import com.example.pastestorage.repositories.PasteRepository;
import com.example.pastestorage.security.AuthorityUtil;
import com.example.pastestorage.types.AccessType;
import com.example.pastestorage.types.UserRole;
import com.example.pastestorage.util.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PasteService {
    private final PasteRepository pasteRepository;
    private final SessionUtil sessionUtil;

    @Transactional
    public void save(Paste paste) {
        pasteRepository.save(paste);
    }
    @Transactional(readOnly = true)
    public Paste get(UUID id) {
        sessionUtil.enableExpiredFilter();
        Paste foundPaste = pasteRepository
                .findById(id)
                .orElseThrow(() -> new PasteNotFoundException("Paste with id " + id + " is not found"));
        sessionUtil.disableFilter("expiredFilter");
        return foundPaste;
    }

    @Transactional
    public Paste delete(UUID id) {
        UserRole requesterRole = AuthorityUtil.getRoleFromContext();
        String requesterUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Paste paste = get(id);
        if (paste.getCreatedBy().getUsername() == requesterUsername
                || AuthorityUtil.getRolePriority(requesterRole) >= AuthorityUtil.getRolePriority(UserRole.ROLE_ADMIN)) {
            pasteRepository.delete(paste);
        } else {
            throw new UnauthorizedActionException("Only " + UserRole.ROLE_ADMIN + " or higher can delete other user's pastes");
        }
        return paste;
    }

    @Transactional(readOnly = true)
    public Page<Paste> getPublic(int page,
                                 int size,
                                 String orderBy,
                                 String orderDirection,
                                 Instant minStartDate,
                                 Instant maxStartDate) {
        Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(orderDirection), orderBy));
        sessionUtil.enableExpiredFilter();
        Page<Paste> pagePastes = pasteRepository.findNotExpired(
                AccessType.PUBLIC,
                minStartDate,
                maxStartDate,
                paging);
        sessionUtil.disableFilter("expiredFilter");
        return pagePastes;
    }

    @Transactional
    @Scheduled(cron = "${cron.clean_database_time_expression}") // Defined in properties file. Cron format
    public void deleteExpired() {
        pasteRepository.deleteExpired(Instant.now());
    }

}
