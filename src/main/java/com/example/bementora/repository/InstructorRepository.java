package com.example.bementora.repository;

import com.example.bementora.entity.InstructorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InstructorRepository extends JpaRepository<InstructorEntity, UUID> {
}
