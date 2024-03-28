package com.example.bilingualb10.service.s3file.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.bilingualb10.service.s3file.S3FileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class S3ServiceImpl implements S3FileService{
    private final S3Client s3;
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket.url}")
    private String BUCKET_PATH;
    @Value("${application.bucket.name}")
    private String BUCKET_NAME;


    @Override
    public Map<String, String> uploadFileTo(MultipartFile file) {
        log.info(" File is uploading ...");
        String key = System.currentTimeMillis() + file.getOriginalFilename();
        String contentType = file.getContentType();
        PutObjectRequest put = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .contentLength(file.getSize())
                .contentType(contentType)
                .build();
        try {
            s3.putObject(put, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            log.info("File is successfully uploaded !!!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Map.of("link", BUCKET_PATH + key);
    }

    @Override
    public byte[] downloadFileFrom(String fileName) {
        S3Object s3Object = amazonS3.getObject(BUCKET_NAME, fileName);
        S3ObjectInputStream objectContent = s3Object.getObjectContent();
        try {
            log.info("File is successfully downloaded !!!");
            return IOUtils.toByteArray(objectContent);
        } catch (IOException e) {
            log.error("Error processing file !!!");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> deleteFile(String fileName){
        log.info("Deleting file...");
        try {
            String key = fileName.substring(BUCKET_PATH.length());
            log.warn("Deleting object: {}", key);
            s3.deleteObject(dor -> dor.bucket(BUCKET_NAME).key(key).build());
        } catch (S3Exception e) {
            throw new IllegalStateException(e.awsErrorDetails().errorMessage());
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
        return Map.of(
                "message", fileName + " has been deleted !!!");
    }
}