package com.example.bementora.repository;

import com.example.bementora.entity.CartEntity;
import com.example.bementora.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository  extends JpaRepository<CartEntity, UUID> {
    Optional<CartEntity> findByUserIdAndStatus(UUID userId, CartStatus status);

    Optional<CartEntity> findBySessionIdAndStatus(UUID sessionId, CartStatus status);

    @Query("SELECT c FROM CartEntity c WHERE c.expiresAt < :now AND c.cartStatus = 'ACTIVE'")
    List<CartEntity> findExpiredCarts(@Param("now") LocalDateTime now);

    @Query("SELECT COUNT(ci) FROM CartEntity c JOIN c.cartItemEntities ci WHERE c.userId = :userId AND c.cartStatus = 'ACTIVE'")
    int countActiveItemsByUserId(@Param("userId") UUID userId);

    @Query("SELECT c FROM CartEntity c JOIN FETCH c.cartItemEntities ci JOIN FETCH ci.course WHERE c.userId = :userId AND c.cartStatus = 'ACTIVE'")
    Optional<CartEntity> findActiveCartWithItemsByUserId(@Param("userId") UUID userId);
}
