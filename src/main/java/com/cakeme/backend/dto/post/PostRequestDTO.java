package com.cakeme.backend.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDTO {
    private String title; // 제목
    private String content; // 내용
    private String category; // 카테고리
    private String attachment; // 첨부파일 경로
}
