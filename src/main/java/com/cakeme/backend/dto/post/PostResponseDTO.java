package com.cakeme.backend.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDTO {
    private Long id; // 게시글 ID
    private String title; // 제목
    private String content; // 내용
    private String category; // 카테고리
    private String attachment; // 첨부파일 경로 (첨부파일이 없으면 null)
    private String author; // 작성자 이름
    private String authorId; // 작성자 ID
}
