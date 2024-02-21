package com.project.controller;

import com.project.dto.request.ImageDTO;
import com.project.dto.response.ImageViewDTO;
import com.project.model.*;
import com.project.service.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
public class ImageController {

    private final ImageService imageService;

    @GetMapping(value = "")
    public ResponseEntity<Result> getAllImage(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<ImageViewDTO> listImage = imageService.getAll(keyword, pageNo, 2);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list image successfully", listImage));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        ImageViewDTO imageDto = imageService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query image by id successfully", imageDto));
    }

    @PostMapping()
    public ResponseEntity<Result> insertImage(@Valid @RequestBody ImageDTO newImage) {
        ImageViewDTO imageDto = imageService.save(newImage);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Insert image successfully", imageDto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Result> updateImage(@Valid @RequestBody ImageDTO newImage, @PathVariable Integer id) {
        ImageViewDTO imageDto = imageService.update(newImage, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update image successfully", imageDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Result> deleteImage(@PathVariable Integer id, @RequestParam(name="check", defaultValue = "false") boolean check) {
        this.imageService.delete(id, check);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Delete image successfully", null));
    }
}
