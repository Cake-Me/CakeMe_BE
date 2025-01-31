// CakeController.java
package com.cakeme.backend.controller;

import com.cakeme.backend.entity.CakeEntity;
import com.cakeme.backend.dto.cake.CakeRequestDTO;
import com.cakeme.backend.dto.response.ResponseDTO;
import com.cakeme.backend.service.CakeService;
import com.cakeme.backend.service.OpenAiService;
import com.cakeme.backend.util.ImageDownloader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public ResponseEntity<ResponseDTO<String>> saveCakeDesign(@RequestBody Map<String, Object> saveRequest) {
        int selectedIndex = (int) saveRequest.get("selectedIndex");
        List<String> images = (List<String>) saveRequest.get("images");

        if (selectedIndex < 0 || selectedIndex >= images.size()) {
            throw new IllegalArgumentException("Invalid image selection index.");
        }

        String imageUrl = images.get(selectedIndex); // 선택된 이미지 URL
        String saveDir = "images"; // 저장할 디렉토리
        String fileName = "cake_image_" + selectedIndex; // 저장할 파일 이름


        try {
            // 이미지 다운로드 및 저장
            String savedImagePath = ImageDownloader.downloadImage(imageUrl, saveDir, fileName);
            return ResponseEntity.ok(new ResponseDTO<>(200, "Image saved successfully", savedImagePath));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(new ResponseDTO<>(500, "Image download failed", null));
        }
    }

}