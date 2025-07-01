package com.example.bementora.repository;

import com.example.bementora.entity.CartEntity;
import com.example.bementora.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository  extends JpaRepository<CartEntity, UUID> {
    Optional<CartEntity> findByUserIdAndStatus(UUID userId, CartStatus status);

    Optional<CartEntity> findBySessionIdAndStatus(UUID sessionId, CartStatus status);
}
