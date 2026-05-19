package com.beknazarovmiras.booking.controller;

import com.beknazarovmiras.booking.service.BeknazarovMirasFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "Files", description = "File download")
public class BeknazarovMirasFileController {

    private final BeknazarovMirasFileService fileService;

    @GetMapping("/{subDir}/{filename}")
    @Operation(summary = "Download a file")
    public ResponseEntity<Resource> download(
            @PathVariable String subDir, @PathVariable String filename) {
        Resource resource = fileService.downloadFile(subDir + "/" + filename);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
