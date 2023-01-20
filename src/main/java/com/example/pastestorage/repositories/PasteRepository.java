package com.example.pastestorage.repositories;

import com.example.pastestorage.models.Paste;
import com.example.pastestorage.types.AccessType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasteRepository extends JpaRepository<Paste, UUID> {
    Optional<Paste> findByIdAndExpireDateGreaterThan(UUID id, Instant expireDate);
    Page<Paste> findByAccessTypeAndExpireDateGreaterThan(
            AccessType accessType,
            Instant expireDate,
            Pageable pageable);
}
