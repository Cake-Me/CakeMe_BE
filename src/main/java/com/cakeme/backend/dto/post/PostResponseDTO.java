package com.cakeme.backend.dto.post;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostResponseDTO {
    private Long id; // 게시글 ID
    private String title; // 제목
    private String content; // 내용
    private String category; // 카테고리
    private String attachment;
    private String author; // 작성자 이름
    private String authorId; // 작성자 ID (추가)
}
