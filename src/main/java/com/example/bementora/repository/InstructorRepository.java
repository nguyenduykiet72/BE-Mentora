package com.example.bementora.repository;

import com.example.bementora.entity.InstructorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InstructorRepository extends JpaRepository<InstructorEntity, UUID> {
    Optional<InstructorEntity> findByUserId(UUID userId);
}
