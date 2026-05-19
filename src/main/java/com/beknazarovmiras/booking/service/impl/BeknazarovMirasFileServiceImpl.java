package com.beknazarovmiras.booking.service.impl;

import com.beknazarovmiras.booking.exception.BeknazarovMirasBadRequestException;
import com.beknazarovmiras.booking.exception.BeknazarovMirasNotFoundException;
import com.beknazarovmiras.booking.service.BeknazarovMirasFileService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class BeknazarovMirasFileServiceImpl implements BeknazarovMirasFileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(Paths.get(uploadDir));
    }

    @Override
    public String uploadFile(MultipartFile file, String subDir) {
        if (file.isEmpty()) throw new BeknazarovMirasBadRequestException("File is empty");
        try {
            String ext = getExtension(file.getOriginalFilename());
            String filename = UUID.randomUUID() + "." + ext;
            Path dir = Paths.get(uploadDir, subDir);
            Files.createDirectories(dir);
            Path target = dir.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            log.info("File uploaded: {}", target);
            return "/api/files/" + subDir + "/" + filename;
        } catch (IOException e) {
            throw new BeknazarovMirasBadRequestException("Could not upload file: " + e.getMessage());
        }
    }

    @Override
    public Resource downloadFile(String filename) {
        try {
            Path file = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists()) throw new BeknazarovMirasNotFoundException("File not found: " + filename);
            return resource;
        } catch (Exception e) {
            throw new BeknazarovMirasNotFoundException("File not found: " + filename);
        }
    }

    @Override
    @Async("taskExecutor")
    public void deleteFile(String fileUrl) {
        if (fileUrl == null) return;
        try {
            String path = fileUrl.replace("/api/files/", "");
            Path file = Paths.get(uploadDir, path);
            Files.deleteIfExists(file);
            log.info("File deleted async: {}", file);
        } catch (IOException e) {
            log.warn("Could not delete file: {}", fileUrl);
        }
    }

    @Async("taskExecutor")
    public CompletableFuture<String> processFileAsync(MultipartFile file, String subDir) {
        String url = uploadFile(file, subDir);
        log.info("Async file processed: {}", url);
        return CompletableFuture.completedFuture(url);
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "bin";
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }
}
