package com.cakeme.backend.repository;

import com.cakeme.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<com.cakeme.backend.entity.UserEntity, Long> {
    Optional<com.cakeme.backend.entity.UserEntity> findByUserId(String userId); // user_id로 검색

    boolean existsByUserId(String userId);
}
