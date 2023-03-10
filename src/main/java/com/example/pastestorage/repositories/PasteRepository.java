package com.example.pastestorage.repositories;

import com.example.pastestorage.models.Paste;
import com.example.pastestorage.types.AccessType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasteRepository extends JpaRepository<Paste, UUID> {
    @Query("SELECT p FROM Paste p WHERE p.id = :id")
    Optional<Paste> findById(@Param("id") UUID id);

    @Query("SELECT p FROM Paste p WHERE p.accessType = :AccessType"
            + " AND p.createdAt >= :minCreatedDate"
            + " AND p.createdAt <= :maxCreatedDate")
    Page<Paste> findNotExpired(@Param("AccessType") AccessType accessType,
                               @Param("minCreatedDate") Instant minCreatedDate,
                               @Param("maxCreatedDate") Instant maxCreatedDate,
                               Pageable pageable);
    @Modifying
    @Query("DELETE FROM Paste p WHERE p.expireDate < :currentDate")
    void deleteExpired(@Param("currentDate") Instant currentDate);
}
