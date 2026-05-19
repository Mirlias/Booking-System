package com.beknazarovmiras.booking.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface BeknazarovMirasFileService {
    String uploadFile(MultipartFile file, String subDir);
    Resource downloadFile(String filename);
    void deleteFile(String fileUrl);
}
