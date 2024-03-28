package com.example.bilingualb10.api;

import com.example.bilingualb10.service.s3file.S3FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import static java.net.HttpURLConnection.HTTP_OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3file")
@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
@Tag(name = "S3 Files API")
@CrossOrigin(origins = "*", maxAge = 3600)
public class S3FileApi {
    private final S3FileService s3FileService;

    @Operation(summary = "Метод для загрузки файла к бакету")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, String> uploadFile(@RequestParam MultipartFile multipartFile) throws IOException {
        return s3FileService.uploadFileTo(multipartFile);
    }

    @Operation(summary = "Метод для загрузки файла с бакета")
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", MediaType.ALL_VALUE);
        String contentDisposition = String.format("attachment; filename=\"%s\"",
                URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        headers.add("Content-Disposition", contentDisposition);
        byte[] bytes = s3FileService.downloadFileFrom(fileName);
        return ResponseEntity.status(HTTP_OK).headers(headers).body(bytes);
    }

    @Operation(summary = "Метод для удаления файла с бакета")
    @DeleteMapping("/delete")
    Map<String, String> deleteFileFrom(@RequestParam String fileName) {
        return s3FileService.deleteFile(fileName);
    }
}