package com.cakeme.backend.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostRequestDTO {

    @Schema(description = "게시글 제목", example = "게시글 제목")
    private String title;

    @Schema(description = "게시글 내용", example = "게시글 내용")
    private String content;

    @Schema(description = "게시글 카테고리", example = "카테고리 이름")
    private String category;


    @Schema(
            description = "첨부파일 (선택 사항)",
            type = "string",
            format = "binary",
            nullable = true  // null 허용

    )
    private MultipartFile attachment;
}
