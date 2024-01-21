package com.project.controller;

import com.project.dto.CategoryDTO;
import com.project.exception.NoCategoryFoundException;
import com.project.model.Category;
import com.project.model.Result;
import com.project.service.CategoryService;
import com.project.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping(value = "/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Get all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of categories"),
            @ApiResponse(responseCode = "404", description = "No categories found")
    })
    @GetMapping(value = "")
    public ResponseEntity<Result> getAllCategory(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
//        List<Category> listCategory = categoryService.getAll();
        Page<Category> listCategory = categoryService.pagination(pageNo, 2);
        if(keyword != null){
            listCategory = this.categoryService.search(keyword, pageNo, 2);
        }
        if (!listCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Query list category successfully", listCategory));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(404, "No categories found", null));
        }
    }

    @Operation(summary = "Get category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Object category"),
            @ApiResponse(responseCode = "404", description = "No category found")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        Category category = categoryService.findById(id);
//        if (category != null) {
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(new Result(200, "Query category successfully", category));
//        } else {
//
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new Result(404, "Cannot find category", null));
//        }
        if(category == null) throw new NoCategoryFoundException("");
        return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Query category successfully", category));
    }

    @PostMapping(value = "")
    public ResponseEntity<Result> insertCategory(@Valid @RequestBody CategoryDTO newCategory,
                                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> errorMessages = errors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Result(400, "Validation failed", errorMessages));
        }
        List<Category> foundCategory = categoryService.findByName(newCategory.getName().trim());
        if (!foundCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new Result(501, "Category name already taken", ""));
        }
        Category categoryRequest = modelMapper.map(newCategory, Category.class);
        Category category = categoryService.saveOrUpdate(categoryRequest);
        CategoryDTO categoryResponse = modelMapper.map(category, CategoryDTO.class);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Insert category successfully", categoryResponse));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Result> updateCategory(@Valid @RequestBody CategoryDTO newCategory,
                                                        BindingResult bindingResult, @PathVariable Integer id) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> errorMessages = errors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Result(400, "Validation failed", errorMessages));
        }
        List<Category> foundCategory = categoryService.findByName(newCategory.getName().trim());
        if (!foundCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new Result(501, "Category name already taken", ""));
        }
        Category updateCategory = categoryService.findById(id);
        if (updateCategory != null) {
            updateCategory.setName(newCategory.getName());
            updateCategory.setStatus(newCategory.getStatus());
            updateCategory = categoryService.saveOrUpdate(updateCategory);
        } else {
            Category categoryRequest = modelMapper.map(newCategory, Category.class);
            categoryRequest.setId(id);
            updateCategory = categoryService.saveOrUpdate(categoryRequest);
        }
        CategoryDTO categoryResponse = modelMapper.map(updateCategory, CategoryDTO.class);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update category successfully", categoryResponse));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Result> deleteCategory(@PathVariable Integer id) {
        boolean exists = categoryService.existsById(id);
        if (exists) {
            productService.deleteByCategory(id);
            categoryService.delete(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Delete category successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Result(404, "Cannot find category to delete", ""));
    }

}
