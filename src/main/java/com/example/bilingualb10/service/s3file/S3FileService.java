package com.example.bilingualb10.service.s3file;

import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface S3FileService {

    Map<String, String> uploadFileTo(MultipartFile file);

    byte[] downloadFileFrom(String fileName);

    Map<String, String> deleteFile(String fileName);
}