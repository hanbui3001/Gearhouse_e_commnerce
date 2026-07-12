package com.example.demo_ecommerce.service;

import com.example.demo_ecommerce.dto.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    List<FileResponse> uploadFile(List<MultipartFile> files);

    FileResponse uploadFileByS2(MultipartFile files) throws IOException;
}
