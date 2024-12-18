package com.cakeme.backend.util;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImageDownloader {

    public static String downloadImage(String imageUrl, String saveDir, String fileName) throws IOException {
        // 저장할 디렉토리 설정
        File directory = new File(saveDir);
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉토리가 없으면 생성
        }

        // 저장할 파일 경로 생성
        String filePath = saveDir + File.separator + fileName + ".jpg";
        File file = new File(filePath);

        // URL에서 이미지 다운로드
        URL url = new URL(imageUrl);
        FileUtils.copyURLToFile(url, file);

        return filePath; // 저장된 파일 경로 반환
    }
}
