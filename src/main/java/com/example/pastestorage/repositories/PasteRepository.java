package com.example.pastestorage.repositories;

import com.example.pastestorage.models.Paste;
import com.example.pastestorage.types.AccessType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasteRepository extends JpaRepository<Paste, UUID> {
    Optional<Paste> findByIdAndExpireDateGreaterThan(UUID id, Instant expireDate);
    @Query("SELECT p FROM Paste p WHERE p.accessType = :AccessType"
            + " AND p.expireDate >= :currentDate"
            + " AND p.createdAt >= :minCreatedDate"
            + " AND p.createdAt <= :maxCreatedDate")
    Page<Paste> findPublicNotExpired(@Param("AccessType") AccessType accessType,
                                     @Param("currentDate") Instant currentDate,
                                     @Param("minCreatedDate") Instant minCreatedDate,
                                     @Param("maxCreatedDate") Instant maxCreatedDate,
                                     Pageable pageable);
    @Modifying
    @Query("DELETE FROM Paste p WHERE p.expireDate < :currentDate")
    void deleteExpired(@Param("currentDate") Instant currentDate);
}
