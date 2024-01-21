package com.project.controller;

import com.project.dto.ImageDTO;
import com.project.dto.ImageViewDTO;
import com.project.model.*;
import com.project.service.ImageService;
import com.project.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
public class ImageController {

    private final ImageService imageService;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @GetMapping(value = "")
    public ResponseEntity<Result> getAllImage(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<Image> listImage = imageService.pagination(pageNo, 5);
        if(keyword != null){
            listImage = this.imageService.search(keyword, pageNo, 5);
        }
        if (!listImage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Query list image successfully", listImage));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(404, "No images found", null));
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        Image image = imageService.findById(id);
        if (image != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Query image successfully", image));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(404, "Cannot find image", null));
        }
    }

    @PostMapping()
    public ResponseEntity<Result> insertImage(@Valid @RequestBody ImageDTO newImage,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> errorMessages = errors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Result(400, "Validation failed", errorMessages));
        }
        Product product = productService.findById(newImage.getProductId());
        newImage.setProduct(product);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        Image imageRequest = modelMapper.map(newImage, Image.class);
        Image image = imageService.saveOrUpdate(imageRequest);
        ImageViewDTO imageResponse = modelMapper.map(image, ImageViewDTO.class);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Insert image successfully", imageResponse));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Result> updateProduct(@Valid @RequestBody ImageDTO newImage,
                                                BindingResult bindingResult, @PathVariable Integer id) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> errorMessages = errors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Result(400, "Validation failed", errorMessages));
        }
        Image updateImage = imageService.findById(id);
        Product product = productService.findById(newImage.getProductId());
        if (updateImage != null) {
            updateImage.setName(newImage.getName());
            updateImage.setProduct(product);
            updateImage = imageService.saveOrUpdate(updateImage);
        } else {
            newImage.setProduct(product);
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            Image imageRequest = modelMapper.map(newImage, Image.class);
            imageRequest.setId(id);
            updateImage = imageService.saveOrUpdate(imageRequest);
        }
        ImageViewDTO imageResponse = modelMapper.map(updateImage, ImageViewDTO.class);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update image successfully", imageResponse));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Result> deleteImage(@PathVariable Integer id) {
        boolean exists = imageService.existsById(id);
        if (exists) {
            imageService.delete(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Delete image successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Result(404, "Cannot find image to delete", ""));
    }
}
