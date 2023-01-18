package com.example.pastestorage.repositories;

import com.example.pastestorage.models.Paste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PasteRepository extends JpaRepository<Paste, UUID> {

}
