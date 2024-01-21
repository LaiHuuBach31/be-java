package com.project.controller;

import com.project.dto.SizeDTO;
import com.project.model.Result;
import com.project.model.Size;
import com.project.service.SizeService;
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
@RequestMapping(value = "/api/v1/size")
public class SizeController {
    private final SizeService sizeService;
    private final ModelMapper modelMapper;

    @GetMapping(value = "")
    public ResponseEntity<Result> getAllSize(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<Size> listSize = sizeService.pagination(pageNo, 5);
        if(keyword != null){
            listSize = this.sizeService.search(keyword, pageNo, 5);
        }
        if (!listSize.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Query list sizes successfully", listSize));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(404, "No sizes found", null));
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<Result> insertSize(@Valid @RequestBody SizeDTO newSize,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> errorMessages = errors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Result(400, "Validation failed", errorMessages));
        }
        List<Size> foundSize = sizeService.findByName(newSize.getName().trim());
        if (!foundSize.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new Result(501, "Size name already taken", ""));
        }
        Size sizeRequest = modelMapper.map(newSize, Size.class);
        Size size = sizeService.saveOrUpdate(sizeRequest);
        SizeDTO sizeResponse = modelMapper.map(size, SizeDTO.class);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Insert size successfully", sizeResponse));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Result> updateSize(@Valid @RequestBody SizeDTO newSize,
                                                 BindingResult bindingResult, @PathVariable Integer id) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> errorMessages = errors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Result(400, "Validation failed", errorMessages));
        }
        List<Size> foundSize = sizeService.findByName(newSize.getName().trim());
        if (!foundSize.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new Result(501, "Size name already taken", ""));
        }
        Size updateSize = sizeService.findById(id);
        if (updateSize != null) {
            updateSize.setName(newSize.getName());
            updateSize.setStatus(newSize.getStatus());
            updateSize = sizeService.saveOrUpdate(updateSize);
        } else {
            Size sizeRequest = modelMapper.map(newSize, Size.class);
            sizeRequest.setId(id);
            updateSize = sizeService.saveOrUpdate(sizeRequest);
        }
        SizeDTO sizeResponse = modelMapper.map(updateSize, SizeDTO.class);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update color successfully", sizeResponse));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Result> deleteSize(@PathVariable Integer id) {
        boolean exists = sizeService.existsById(id);
        if (exists) {
            sizeService.delete(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Delete size successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Result(404, "Cannot find size to delete", ""));
    }
}
