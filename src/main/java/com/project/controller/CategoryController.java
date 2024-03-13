package com.project.controller;

import com.project.dto.request.CategoryDTO;
import com.project.model.Result;
import com.project.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping(value = "/all")
    public ResponseEntity<Result> getAllCategory() {
        List<CategoryDTO> listCategory = categoryService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list category successfully", listCategory));
    }

    @Operation(summary = "Get all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of categories")
    })
    @GetMapping(value = "")
    public ResponseEntity<Result> getAllCategory(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<CategoryDTO> listCategory = categoryService.getAll(keyword, pageNo, 10);
        return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Query list category successfully", listCategory));
    }

    @Operation(summary = "Get category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Object category"),
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        CategoryDTO categoryDto = categoryService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query category by id successfully", categoryDto));
    }

    @PostMapping()
    public ResponseEntity<Result> insertCategory( @Valid @RequestBody CategoryDTO newCategory) {
        CategoryDTO categoryDto = categoryService.save(newCategory);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Insert category successfully", categoryDto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Result> updateCategory( @Valid @RequestBody CategoryDTO newCategory, @PathVariable Integer id) {
        CategoryDTO categoryDto = categoryService.update(newCategory, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update category successfully", categoryDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Result> deleteCategory(@PathVariable Integer id, @RequestParam(name="check", defaultValue = "false") boolean check) {
        this.categoryService.delete(id, check);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Delete category successfully", null));
    }

}
