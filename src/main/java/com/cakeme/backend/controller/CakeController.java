// CakeController.java
package com.cakeme.backend.controller;

import com.cakeme.backend.entity.CakeEntity;
import com.cakeme.backend.dto.cake.CakeRequestDTO;
import com.cakeme.backend.dto.response.ResponseDTO;
import com.cakeme.backend.service.CakeService;
import com.cakeme.backend.service.OpenAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cake")
public class CakeController {

    private final OpenAiService openAiService;
    private final CakeService cakeService;

    public CakeController(OpenAiService openAiService, CakeService cakeService) {
        this.openAiService = openAiService;
        this.cakeService = cakeService;
    }

    @PostMapping("/design")
    public ResponseEntity<Map<String, Object>> generateCakeDesign(@RequestBody CakeRequestDTO request) {
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

        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO<CakeEntity>> saveCakeDesign(@RequestBody Map<String, Object> saveRequest) {
        // 요청 데이터 추출
        int selectedIndex = (int) saveRequest.get("selectedIndex"); // 선택된 이미지의 인덱스
        List<String> images = (List<String>) saveRequest.get("images"); // 이미지 리스트

        if (selectedIndex < 0 || selectedIndex >= images.size()) {
            throw new IllegalArgumentException("Invalid image selection index.");
        }

        String imageUrl = images.get(selectedIndex); // 선택된 이미지 URL

        CakeRequestDTO request = new CakeRequestDTO(
                (String) saveRequest.get("shape"),
                (String) saveRequest.get("flavor"),
                (String) saveRequest.get("color1"),
                (String) saveRequest.get("color2"),
                (String) saveRequest.get("occasion"),
                (String) saveRequest.get("theme"),
                (String) saveRequest.get("text")
        );

        // 케이크 엔티티 저장
        CakeEntity savedCake = cakeService.saveCakeDesign(request, imageUrl);

        return ResponseEntity.ok(new ResponseDTO<>(200, "Cake design saved successfully", savedCake));
    }
}