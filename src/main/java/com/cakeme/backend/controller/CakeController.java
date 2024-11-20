package com.cakeme.backend.controller;

import com.cakeme.backend.entity.CakeEntity;
import com.cakeme.backend.dto.cake.CakeRequestDTO;
import com.cakeme.backend.service.CakeService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cake")
public class CakeController {

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

        // 메시지 생성
        String message = String.format(
                "%s 모양의 케이크를 만들고 싶어 케이크의 맛은 %s 맛으로 할거야 그리고 %s, %s 이 두 개의 색이 케이크 크림 색으로 쓰이게 하고 싶어. " +
                        "이 케이크를 만드는 이유는 %s을 감사한/축하하는 마음을 전하기 위해서야 %s 분위기로 %s라고 적히도록 케이크 디자인을 따로 3개 만들어서 사진 3개를 보내줘. " +
                        "그리고 케이크 외에 배경은 없었으면 좋겠어.",
                shape, flavor, color1, color2, occasion, theme, text
        );

        // 이미지 URL 리스트 생성 (가상 URL)
        List<String> images = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            images.add("https://example.com/cake_image_" + i + ".jpg");
        }

        // 응답 생성
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("images", images);

        return response;
    }
}
