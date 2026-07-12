package com.example.demo_ecommerce.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo_ecommerce.dto.response.FileResponse;
import com.example.demo_ecommerce.exception.CustomException;
import com.example.demo_ecommerce.exception.ErrorCode;
import com.example.demo_ecommerce.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    private final Cloudinary cloudinary;
    private final Executor uploadExecutor;
    private final S3Client s3Client;
    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    @Value("${aws.s3.region}")
    private String region;

    public FileServiceImpl(Cloudinary cloudinary,
                           @Qualifier("uploadExecuter") Executor uploadExecutor,
                           S3Client s3Client) {
        this.cloudinary = cloudinary;
        this.uploadExecutor = uploadExecutor;
        this.s3Client = s3Client;
    }

    @Override
    public List<FileResponse> uploadFile(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return List.of();
        }
        for (MultipartFile file : files) {
            if (file.getSize() >= 5 * 1024 * 1024) {
                throw new CustomException(ErrorCode.FILE_LIMIT_CAPACITY);
            }
            if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
                throw new CustomException(ErrorCode.UNSUPPORTED_MEDIA_TYPE);

            }
        }
        List<CompletableFuture<FileResponse>> futures = files.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    try {
                        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                                "folder", "Gearhouse",
                                "resource_type", "image",
                                "use_filename", true,
                                "overwrite", false
                        ));
                        String secureUrl = uploadResult.get("secure_url").toString();
                        double sizeMb = Math.round((file.getSize() / (1024.0 * 1024.0)) * 100.0) / 100.0;
                        return FileResponse.builder()
                                .fileName(file.getOriginalFilename())
                                .fileType(file.getContentType())
                                .size(sizeMb)
                                .url(secureUrl)
                                .build();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }, uploadExecutor)).toList();
        return futures.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    @Override
    public FileResponse uploadFileByS2(MultipartFile files) throws IOException {
        String key = generateKey(files.getOriginalFilename());
        Double sizeMb = Math.round(files.getSize() / (1024.0 * 1024.0) * 100) / 100.0;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .key(key)
                .bucket(bucketName)
                .contentType(files.getContentType())
                .build();
        RequestBody requestBody = RequestBody.fromInputStream(files.getInputStream(), files.getSize());
        var putObjectResponse = s3Client.putObject(putObjectRequest, requestBody);
        if (putObjectResponse.sdkHttpResponse().isSuccessful()) {
            log.info("upload file successfully");
        }
        String url = "https://" + bucketName + ".s3." + region + "amazonaws.com/" + key;
        return FileResponse.builder()
                .fileName(key)
                .fileType(files.getContentType())
                .size(sizeMb)
                .url(url)
                .build();
    }

    private String generateKey(String originalFilename) {
        if (!StringUtils.hasText(originalFilename)) {
            return System.currentTimeMillis() + "_" + originalFilename;
        } else {
            return originalFilename.substring(0, originalFilename.indexOf("."))
                    + "_" + System.currentTimeMillis()
                    + originalFilename.substring(originalFilename.indexOf("."));
        }
    }
}
