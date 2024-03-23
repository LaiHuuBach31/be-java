package com.project.controller;


import com.project.dto.request.ItemDTO;
import com.project.dto.request.ProductDTO;
import com.project.dto.response.ProductViewDTO;
import com.project.model.Result;
import com.project.service.ProductService;
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
@RequestMapping(value = "/api/v1/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/filters")
    public ResponseEntity<Result> filterProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(required = false) Integer colorId,
            @RequestParam(required = false) Integer sizeId,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder
    ) {
        Page<ProductViewDTO> products = productService.getAll(keyword, categoryId, minPrice, maxPrice, colorId, sizeId, pageNo, pageSize, sortBy, sortOrder);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list product successfully", products));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<Result> getAllProduct() {
        List<ProductViewDTO> listProduct = productService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list item successfully", listProduct));
    }
    @GetMapping()
    public ResponseEntity<Result> getAllProduct(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<ProductViewDTO> listProduct = productService.getAll(keyword, pageNo, 8);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list product successfully", listProduct));
    }
    @GetMapping("/filter")
    public ResponseEntity<Result> filterByCategory(@Param("categoryId") Integer categoryId,@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<ProductViewDTO> listProduct = productService.filter(categoryId, pageNo, 8);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list product by category successfully", listProduct));
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        ProductViewDTO productDto = productService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query product by id successfully", productDto));
    }

    @PostMapping()
    public ResponseEntity<Result> insertProduct(@Valid @RequestBody ProductDTO newProduct) {
        ProductViewDTO productDto = productService.save(newProduct);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Insert product successfully", productDto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Result> updateProduct(@Valid @RequestBody ProductDTO newProduct, @PathVariable Integer id) {
        ProductViewDTO productDto = productService.update(newProduct, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update product successfully", productDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Result> deleteCategory(@PathVariable Integer id, @RequestParam(name="check", defaultValue = "false") boolean check) {
        productService.delete(id, check);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Delete product successfully", null));
    }
}
