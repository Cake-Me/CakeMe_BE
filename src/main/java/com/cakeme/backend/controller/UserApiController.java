package com.cakeme.backend.controller;

import com.cakeme.backend.dto.user.AddUserRequestDTO;
import com.cakeme.backend.dto.response.ResponseDTO;
import com.cakeme.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/signup")
public class UserApiController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원 정보를 입력받아 새 유저를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "409", description = "중복된 사용자 ID"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    public ResponseEntity<ResponseDTO<Map<String, Object>>> signup(@Valid @RequestBody AddUserRequestDTO request) {
        if (userService.isUserIdDuplicated(request.getUserId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO<>(409, "이미 사용 중인 사용자 ID입니다.", null));
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO<>(400, "비밀번호가 일치하지 않습니다.", null));
        }

        Long userId = userService.save(request);
        Map<String, Object> data = Map.of(
                "id", userId,
                "username", request.getUsername(),
                "userId", request.getUserId()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(201, "회원가입 성공", data));
    }
}
