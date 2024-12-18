// CakeService.java
package com.cakeme.backend.service;

import com.cakeme.backend.dto.cake.CakeRequestDTO;
import com.cakeme.backend.entity.CakeEntity;
import com.cakeme.backend.repository.CakeRepository;
import org.springframework.stereotype.Service;

@Service
public class CakeService {
    private final CakeRepository cakeRepository;

    public CakeService(CakeRepository cakeRepository) {
        this.cakeRepository = cakeRepository;
    }

    public CakeEntity saveCakeDesign(CakeRequestDTO request, String imageUrl) {
        CakeEntity cakeEntity = CakeEntity.builder()
                .shape(request.getShape())
                .flavor(request.getFlavor())
                .color1(request.getColor1())
                .color2(request.getColor2())
                .occasion(request.getOccasion())
                .theme(request.getTheme())
                .text(request.getText())
                .imageUrl(imageUrl)
                .build();

        return cakeRepository.save(cakeEntity);
    }
}
