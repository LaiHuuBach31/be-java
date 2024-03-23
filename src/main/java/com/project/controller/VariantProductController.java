package com.project.controller;

import com.project.dto.request.CategoryDTO;
import com.project.dto.request.VariantProductDTO;
import com.project.dto.response.VariantProductViewDTO;
import com.project.model.*;
import com.project.service.VariantProductService;
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
@RequestMapping(value = "/api/v1/variant")
public class VariantProductController {

    private final VariantProductService variantProductService;

    @GetMapping(value = "/all")
    public ResponseEntity<Result> getAllVariantProduct() {
        List<VariantProductViewDTO> list = variantProductService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list category successfully", list));
    }

    @GetMapping(value = "")
    public ResponseEntity<Result> getAllVariantProduct(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<VariantProductViewDTO> listCategory = variantProductService.getAll(keyword, pageNo, 10);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list variant product successfully", listCategory));
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        VariantProductViewDTO variantProductDto = variantProductService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query variant product by id successfully", variantProductDto));
    }

    @PostMapping()
    public ResponseEntity<Result> insertVariantProduct(@Valid @RequestBody VariantProductDTO newVariantProduct) {
        VariantProductViewDTO variantProductDto = variantProductService.save(newVariantProduct);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Insert variant product successfully", variantProductDto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Result> updateVariantProduct(@Valid @RequestBody VariantProductDTO newVariantProduct,@PathVariable Integer id) {
        VariantProductViewDTO variantProductDto = variantProductService.update(newVariantProduct, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update variant product successfully", variantProductDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Result> deleteVariantProduct(@PathVariable Integer id, @RequestParam(name="check", defaultValue = "false") boolean check) {
        variantProductService.delete(id, check);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Delete variant product successfully", null));
    }

    @GetMapping(value = "/list/{id}")
    public ResponseEntity<Result> listVariant(@PathVariable Integer id) {
        List<VariantProductViewDTO>  variantProductDto = variantProductService.listVariant(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query variant product by id successfully", variantProductDto));
    }
}
