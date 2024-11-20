//package com.cakeme.backend.service;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class OpenAiService {
//
//    private static final String OPENAI_API_URL = "https://api.openai.com/v1/images/generations";
//
//    @Value("${openai.key}")
//    private String apiKey; // 여기에 OpenAI API 키를 입력하세요.
//
//    public List<String> generateImages(String message) {
//        // HTTP 요청 헤더 설정
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + apiKey);
//        headers.set("Content-Type", "application/json");
//
//        // 요청 본문 구성
//        Map<String, Object> requestBody = Map.of(
//                "prompt", message,
//                "n", 3, // 3개의 이미지를 생성
//                "size", "1024x1024"
//        );
//
//        // RestTemplate으로 API 호출
//        RestTemplate restTemplate = new RestTemplate();
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
//
//        try {
//            ResponseEntity<Map> response = restTemplate.exchange(
//                    OPENAI_API_URL,
//                    HttpMethod.POST,
//                    request,
//                    Map.class
//            );
//
//            // OpenAI 응답에서 이미지 URL 추출
//            List<Map<String, String>> data = (List<Map<String, String>>) response.getBody().get("data");
//            List<String> imageUrls = new ArrayList<>();
//            for (Map<String, String> item : data) {
//                imageUrls.add(item.get("url"));
//            }
//            return imageUrls;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("OpenAI API 호출 중 오류 발생");
//        }
//    }
//}

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
//        headers.add("Content-Type", "proj_dHhmbutnlNx8SpVAqYJwT4oB");
        headers.add("Content-Type", "application/json");

        // 요청 본문 구성
        Map<String, Object> requestBody = Map.of(
                "prompt", message,
                "model", "dall-e-3",
                "n", 1, // 3개의 이미지를 생성
                "size", "1024x1024",
                "quality", "standard"
        );

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    OPENAI_API_URL,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            // OpenAI 응답에서 이미지 URL 추출
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, String>> data = (List<Map<String, String>>) response.getBody().get("data");

            List<String> imageUrls = new ArrayList<>();
            if (data != null) {
                for (Map<String, String> item : data) {
                    imageUrls.add(item.get("url"));
                }
            }
            return imageUrls;

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
    }
}
