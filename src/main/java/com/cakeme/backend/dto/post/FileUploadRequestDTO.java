package com.cakeme.backend.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileUploadRequestDTO {
    @Schema(type = "string", format = "binary", description = "업로드할 파일")
    private MultipartFile file;
}

