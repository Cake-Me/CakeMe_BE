package com.cakeme.backend.controller;

import com.cakeme.backend.entity.CakeEntity;
import com.cakeme.backend.dto.cake.CakeRequestDTO;
import com.cakeme.backend.service.CakeService;
import com.cakeme.backend.service.OpenAiService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cakeme.backend.dto.cake.CakeRequestDTO;

@RestController
@RequestMapping("/cake")
public class CakeController {

    private final OpenAiService openAiService;

    public CakeController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/design")
    public Map<String, Object> generateCakeDesign(@RequestBody CakeRequestDTO request) {
        // 메시지 구성
        String message = String.format(
                "%s shaped cake with %s flavor, decorated with %s and %s as cream colors, made for a %s occasion, " +
                        "designed in a %s theme with '%s' written on it. No background.",
                request.getShape(), request.getFlavor(), request.getColor1(), request.getColor2(),
                request.getOccasion(), request.getTheme(), request.getText()
        );

        // OpenAI API로 이미지 생성
        List<String> images = openAiService.generateImages(message);

        // 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("images", images);

        return response;
    }
}
