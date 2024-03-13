package com.project.controller;


import com.project.dto.request.CategoryDTO;
import com.project.dto.request.SizeDTO;
import com.project.model.Result;
import com.project.service.SizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/size")
public class SizeController {
    private final SizeService sizeService;

    @GetMapping(value = "/all")
    public ResponseEntity<Result> getAllSize() {
        List<SizeDTO> listSize = sizeService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list size successfully", listSize));
    }
    @GetMapping(value = "")
    public ResponseEntity<Result> getAllSize(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<SizeDTO> listSize = sizeService.getAll(keyword, pageNo, 10);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list size successfully", listSize));
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        SizeDTO sizeDto = sizeService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query category by id successfully", sizeDto));
    }

    @PostMapping(value = "")
    public ResponseEntity<Result> insertSize(@Valid @RequestBody SizeDTO newSize) {
        SizeDTO sizeDto = sizeService.save(newSize);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Insert size successfully", sizeDto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Result> updateSize(@Valid @RequestBody SizeDTO newSize, @PathVariable Integer id) {
        SizeDTO sizeDto = sizeService.update(newSize, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update size successfully", sizeDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Result> deleteSize(@PathVariable Integer id, @RequestParam(name="check", defaultValue = "false") boolean check) {
        this.sizeService.delete(id, check);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Delete size successfully", null));
    }
}
