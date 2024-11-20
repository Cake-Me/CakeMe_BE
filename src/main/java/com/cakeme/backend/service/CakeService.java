package com.cakeme.backend.service;

import com.cakeme.backend.entity.CakeEntity;
import com.cakeme.backend.repository.CakeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CakeService {
    private final CakeRepository cakeRepository;

    public CakeService(CakeRepository cakeRepository) {
        this.cakeRepository = cakeRepository;
    }

    public List<CakeEntity> getCakes(String shape, String flavor, String color1, String color2, String occasion, String theme, String text) {
        return cakeRepository.findByShapeAndFlavorAndColor1AndColor2AndOccasionAndThemeAndText(shape, flavor, color1, color2,  occasion, theme, text);
    }
}
