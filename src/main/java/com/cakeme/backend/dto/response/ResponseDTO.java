package com.cakeme.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDTO<T> {
    private int status; // HTTP 상태 코드
    private String message; // 응답 메시지
    private T data; // 응답 데이터
}
