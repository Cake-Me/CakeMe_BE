package com.cakeme.backend.repository;

import com.cakeme.backend.entity.CakeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface CakeRepository extends JpaRepository<CakeEntity, Long> {
    List<CakeEntity> findByShapeAndFlavorAndColor1AndColor2AndOccasionAndThemeAndText(
            String shape, String flavor, String color1, String color2, String occasion, String theme, String text
    );
}