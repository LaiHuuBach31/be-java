package com.project.controller;

import com.project.model.Result;
import com.project.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileUploadController {

    private final StorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<Result> uploadFile(@RequestParam("files") MultipartFile file) {
        try {
            String generatedFileName = storageService.storeFile(file);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "upload file successfully", generatedFileName));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new Result(501, e.getMessage(), ""));
        }
    }

    @PostMapping("/uploads")
    public ResponseEntity<Result> MultiUploadFile(@RequestParam("files") MultipartFile[]  files) {
        for (MultipartFile file: files) {
            if (file.isEmpty()) {
                continue;
            }
            try {
                String generatedFileName = storageService.storeFile(file);
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new Result(200, "uploads file successfully", generatedFileName));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                        .body(new Result(501, e.getMessage(), ""));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Result(400, "No files uploaded", ""));
    }

    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<byte[]> readDetailFile(@PathVariable String fileName) {
        try {
            byte[] bytes = storageService.readFileContent(fileName);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<Result> getUploadFiles() {
        try {
            List<String> urls = storageService.loadAll().map(path -> MvcUriComponentsBuilder
                    .fromMethodName(FileUploadController.class, "readDetailFile", path.getFileName().toString())
                    .build().toUri().toString()).collect(Collectors.toList());
            return ResponseEntity.ok(new Result(200, "List files successfully", urls));
        } catch (Exception e) {
            return ResponseEntity.ok(new Result(400, "List files failed", new String[] {}));
        }
    }
}
