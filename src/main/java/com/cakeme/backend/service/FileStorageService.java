package com.cakeme.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final String uploadDir = System.getProperty("user.dir") + "/uploads/"; // 프로젝트 루트 경로에 업로드 디렉토리 생성


    public String storeFile(MultipartFile file) throws IOException {
        // 디렉토리가 존재하지 않으면 생성
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리 생성
        }

        // 파일 저장 경로 설정
        String filePath = uploadDir + file.getOriginalFilename();

        // 파일 저장
        file.transferTo(new File(filePath));

        return filePath;
    }
}
