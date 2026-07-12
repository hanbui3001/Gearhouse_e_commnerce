package com.example.demo_ecommerce.controller;

import com.example.demo_ecommerce.dto.response.ApiResponse;
import com.example.demo_ecommerce.dto.response.FileResponse;
import com.example.demo_ecommerce.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/files")
public class FileController {
    private final FileService fileService;

    @PostMapping
    public ApiResponse<List<FileResponse>> uploadFile(@RequestParam("files") List<MultipartFile> files) {
        var response = fileService.uploadFile(files);
        return ApiResponse.<List<FileResponse>>builder()
                .code(HttpStatus.CREATED.value())
                .message("upload files successfully")
                .data(response)
                .build();
    }

    @PostMapping("/s3")
    public ApiResponse<FileResponse> uploadS3(@RequestParam("file") MultipartFile file) throws IOException {
        var data = fileService.uploadFileByS2(file);
        return ApiResponse.<FileResponse>builder()
                .code(HttpStatus.OK.value())
                .message("upload file successfully")
                .data(data)
                .build();
    }
}

