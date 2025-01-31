package com.cakeme.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/images/generations";

    @Value("${openai.key}")
    private String apiKey;

    public List<String> generateImages(String message) {
        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + apiKey);
        headers.add("Content-Type", "application/json");

        // 요청 본문 구성
        Map<String, Object> requestBody = Map.of(
                "prompt", message,
                "model", "dall-e-3",
                "n", 1, // 한 번에 1개의 이미지만 생성
                "size", "1024x1024",
                "quality", "standard"
        );

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        List<String> imageUrls = new ArrayList<>();
        try {
            // API를 3번 호출하여 3개의 이미지 생성
            for (int i = 0; i < 3; i++) {
                ResponseEntity<Map> response = restTemplate.exchange(
                        OPENAI_API_URL,
                        HttpMethod.POST,
                        request,
                        Map.class
                );

                // OpenAI 응답에서 이미지 URL 추출
                List<Map<String, String>> data = (List<Map<String, String>>) response.getBody().get("data");
                if (data != null && !data.isEmpty()) {
                    imageUrls.add(data.get(0).get("url")); // 첫 번째 이미지 URL만 추가
                }
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // OpenAI API 호출 에러 처리
            String errorResponse = e.getResponseBodyAsString();
            System.err.println("OpenAI API 오류: " + errorResponse);
            throw new RuntimeException("OpenAI API 호출 실패: " + errorResponse, e);
        } catch (Exception e) {
            // 일반 예외 처리
            e.printStackTrace();
            throw new RuntimeException("OpenAI API 호출 중 알 수 없는 오류 발생", e);
        }

        return imageUrls; // 3개의 이미지 URL 반환
    }

}

