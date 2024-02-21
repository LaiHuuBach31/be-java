package com.project.controller;


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

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<Result> getAllProduct(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<ProductViewDTO> listProduct = productService.getAll(keyword, pageNo, 100);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list product successfully", listProduct));
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
