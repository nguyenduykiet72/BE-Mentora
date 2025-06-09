package com.example.bementora.repository;

import com.example.bementora.entity.RefreshTokenEntity;
import com.example.bementora.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    Optional<RefreshTokenEntity> findByToken(String token);

    @Modifying
    @Query("DELETE FROM RefreshTokenEntity rt WHERE rt.user = ?1")
    void deleteByUser(UserEntity userId);

    @Modifying
    @Query("UPDATE RefreshTokenEntity rt SET rt.revoked = true WHERE rt.user = ?1 AND rt.revoked = false")
    void revokeAllTokensByUser(UserEntity userId);

    @Modifying
    @Query("DELETE FROM RefreshTokenEntity rt WHERE rt.expiryDate < ?1")
    void deleteExpiredTokensBefore(Instant now);
}
