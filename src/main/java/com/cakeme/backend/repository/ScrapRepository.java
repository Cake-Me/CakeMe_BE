package com.cakeme.backend.repository;

import com.cakeme.backend.entity.PostEntity;
import com.cakeme.backend.entity.ScrapEntity;
import com.cakeme.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScrapRepository extends JpaRepository<ScrapEntity, Long> {
    List<ScrapEntity> findByUser(UserEntity user);
    boolean existsByUserAndPost(UserEntity user, PostEntity post);
    void deleteByUserAndPost(UserEntity user, PostEntity post);
}
