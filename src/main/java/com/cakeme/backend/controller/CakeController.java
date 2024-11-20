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

@RestController
@RequestMapping("/cake")
public class CakeController {

    private final OpenAiService openAiService;

    public CakeController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/design")
    public Map<String, Object> generateCakeDesign(@RequestBody Map<String, Object> request) {
        // 입력 데이터 추출
        String shape = (String) request.get("shape");
        String flavor = (String) request.get("flavor");
        String color1 = (String) request.get("color1");
        String color2 = (String) request.get("color2");
        String occasion = (String) request.get("occasion");
        String theme = (String) request.get("theme");
        String text = (String) request.get("text");

        // 메시지 구성
        String message = String.format(
                "%s shaped cake with %s flavor, decorated with %s and %s as cream colors, made for a %s occasion, " +
                        "designed in a %s theme with '%s' written on it. No background.",
                shape, flavor, color1, color2, occasion, theme, text
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
