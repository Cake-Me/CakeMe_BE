package com.cakeme.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<com.cakeme.backend.domain.UserEntity, Long> {
    Optional<com.cakeme.backend.domain.UserEntity> findByUserId(String userId); // user_id로 검색

    boolean existsByUserId(String userId);
}
