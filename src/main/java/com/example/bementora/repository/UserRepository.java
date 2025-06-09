package com.example.bementora.repository;

import com.example.bementora.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);
}
