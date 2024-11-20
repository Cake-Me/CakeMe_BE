package com.cakeme.backend.controller;

import com.cakeme.backend.dto.response.ResponseDTO;
import com.cakeme.backend.dto.user.LoginRequestDTO;
import com.cakeme.backend.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<ResponseDTO<Map<String, Object>>> login(@RequestBody LoginRequestDTO request) {
        try {
            // 사용자 인증
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserId(), request.getPassword())
            );

            // JWT 발급
            String token = jwtTokenProvider.generateToken(request.getUserId());
            Map<String, Object> data = Map.of(
                    "token", token,
                    "userId", request.getUserId()
            );

            return ResponseEntity.ok(new ResponseDTO<>(200, "로그인 성공", data));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO<>(401, "로그인 실패: 아이디 또는 비밀번호를 확인하세요.", null));
        }
    }
}
