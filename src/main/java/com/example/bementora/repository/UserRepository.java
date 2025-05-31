package com.example.bementora.repository;

import com.example.bementora.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class UserRepository implements JpaRepository<UserEntity, Long> {
}
