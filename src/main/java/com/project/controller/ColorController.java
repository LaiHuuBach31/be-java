package com.project.controller;

import com.project.dto.request.CategoryDTO;
import com.project.dto.request.ColorDTO;
import com.project.model.Result;
import com.project.service.ColorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/color")
public class ColorController {

    private final ColorService colorService;

    @GetMapping(value = "/all")
    public ResponseEntity<Result> getAllCategory() {
        List<ColorDTO> listColor = colorService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list color successfully", listColor));
    }

    @GetMapping(value = "")
    public ResponseEntity<Result> getAllColor(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<ColorDTO> listColor = colorService.getAll(keyword, pageNo, 10);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list color successfully", listColor));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        ColorDTO colorDto = colorService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query color by id successfully", colorDto));
    }

    @PostMapping(value = "")
    public ResponseEntity<Result> insertColor(@Valid @RequestBody ColorDTO newColor) {
        ColorDTO colorDto = colorService.save(newColor);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Insert color successfully", colorDto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Result> updateColor(@Valid @RequestBody ColorDTO newColor, @PathVariable Integer id) {
        ColorDTO colorDto = colorService.update(newColor, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update color successfully", colorDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Result> deleteColor(@PathVariable Integer id, @RequestParam(name="check", defaultValue = "false") boolean check) {
        this.colorService.delete(id, check);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Delete color successfully", null));
    }

}
