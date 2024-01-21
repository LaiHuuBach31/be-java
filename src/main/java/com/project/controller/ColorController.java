package com.project.controller;

import com.project.dto.ColorDTO;
import com.project.model.Color;
import com.project.model.Result;
import com.project.service.ColorService;
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
@RequestMapping(value = "/api/v1/color")
public class ColorController {

    private final ColorService colorService;
    private final ModelMapper modelMapper;

    @GetMapping(value = "")
    public ResponseEntity<Result> getAllColor(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<Color> listColor = colorService.pagination(pageNo, 5);
        if(keyword != null){
            listColor = this.colorService.search(keyword, pageNo, 5);
        }
        if (!listColor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Query list colors successfully", listColor));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(404, "No colors found", null));
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<Result> insertColor(@Valid @RequestBody ColorDTO newColor,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> errorMessages = errors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Result(400, "Validation failed", errorMessages));
        }
        List<Color> foundColor = colorService.findByName(newColor.getName().trim());
        if (!foundColor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new Result(501, "Color name already taken", ""));
        }
        Color colorRequest = modelMapper.map(newColor, Color.class);
        Color color = colorService.saveOrUpdate(colorRequest);
        ColorDTO colorResponse = modelMapper.map(color, ColorDTO.class);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Insert color successfully", colorResponse));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Result> updateColor(@Valid @RequestBody ColorDTO newColor,
                                                 BindingResult bindingResult, @PathVariable Integer id) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> errorMessages = errors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Result(400, "Validation failed", errorMessages));
        }
        List<Color> foundColor = colorService.findByName(newColor.getName().trim());
        if (!foundColor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new Result(501, "Color name already taken", ""));
        }
        Color updateColor = colorService.findById(id);
        if (updateColor != null) {
            updateColor.setName(newColor.getName());
            updateColor.setStatus(newColor.getStatus());
            updateColor = colorService.saveOrUpdate(updateColor);
        } else {
            Color colorRequest = modelMapper.map(newColor, Color.class);
            colorRequest.setId(id);
            updateColor = colorService.saveOrUpdate(colorRequest);
        }
        ColorDTO colorResponse = modelMapper.map(updateColor, ColorDTO.class);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update color successfully", colorResponse));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Result> deleteColor(@PathVariable Integer id) {
        boolean exists = colorService.existsById(id);
        if (exists) {
            colorService.delete(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Delete color successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Result(404, "Cannot find color to delete", ""));
    }
}
