package com.example.bementora.repository;

import com.example.bementora.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, UUID> {
    Optional<CartItemEntity> findById(UUID id);

    List<CartItemEntity> findByCartId(UUID cartId);

    @Query("SELECT ci FROM CartItemEntity ci JOIN FETCH ci.course WHERE ci.cartId = :cartId")
    List<CartItemEntity> findByCartIdWithCourse(@Param("cartId") UUID cartId);

    boolean existsByCartIdAndCourseId(UUID cartId, UUID courseId);

    void deleteByCartId(UUID cartId);
}
