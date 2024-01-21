package com.project.controller;

import com.project.dto.VariantProductDTO;
import com.project.dto.VariantProductViewDTO;
import com.project.model.*;
import com.project.service.ColorService;
import com.project.service.ProductService;
import com.project.service.SizeService;
import com.project.service.VariantProductService;
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
@RequestMapping(value = "/api/v1/variant")
public class VariantProductController {

    private final VariantProductService variantProductService;
    private final ProductService productService;
    private final ColorService colorService;
    private final SizeService sizeService;
    private final ModelMapper modelMapper;

    @GetMapping(value = "")
    public ResponseEntity<Result> getAllVariantProduct(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<VariantProduct> listVariantProduct = variantProductService.pagination(pageNo, 2);

        if (!listVariantProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Query list variant product successfully", listVariantProduct));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(404, "No variant product found", null));
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        VariantProduct variantProduct = variantProductService.findById(id);
        if (variantProduct != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Query variant product successfully", variantProduct));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(404, "Cannot find variant product", null));
        }
    }

    @PostMapping()
    public ResponseEntity<Result> insertVariantProduct(@Valid @RequestBody VariantProductDTO newVariantProduct,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> errorMessages = errors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Result(400, "Validation failed", errorMessages));
        }
        VariantProduct checkAVariantProduct = variantProductService.checkAttributes(newVariantProduct.getProductId(), newVariantProduct.getColorId(), newVariantProduct.getSizeId());
        if(checkAVariantProduct != null){
            checkAVariantProduct.setQuantity(checkAVariantProduct.getQuantity() + newVariantProduct.getQuantity());
            variantProductService.saveOrUpdate(checkAVariantProduct);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Insert variant product successfully", checkAVariantProduct));
        }
        Product product = productService.findById(newVariantProduct.getProductId());
        Color color = colorService.findById(newVariantProduct.getColorId());
        Size size = sizeService.findById(newVariantProduct.getSizeId());
        newVariantProduct.setProduct(product);
        newVariantProduct.setSize(size);
        newVariantProduct.setColor(color);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        VariantProduct variantProductRequest = modelMapper.map(newVariantProduct, VariantProduct.class);
        VariantProduct variantProduct = variantProductService.saveOrUpdate(variantProductRequest);
        VariantProductViewDTO variantProductResponse = modelMapper.map(variantProduct, VariantProductViewDTO.class);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Insert variant product successfully", variantProductResponse));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Result> updateVariantProduct(@Valid @RequestBody VariantProductDTO newVariantProduct,
                                                BindingResult bindingResult, @PathVariable Integer id) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> errorMessages = errors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Result(400, "Validation failed", errorMessages));
        }
        VariantProduct checkAVariantProduct = variantProductService.checkAttributes(newVariantProduct.getProductId(), newVariantProduct.getColorId(), newVariantProduct.getSizeId());
        if(checkAVariantProduct != null){
            checkAVariantProduct.setQuantity(checkAVariantProduct.getQuantity() + newVariantProduct.getQuantity());
            variantProductService.saveOrUpdate(checkAVariantProduct);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Insert variant product successfully", checkAVariantProduct));
        }
        VariantProduct updateVariantProduct = variantProductService.findById(id);
        Product product = productService.findById(newVariantProduct.getProductId());
        Color color = colorService.findById(newVariantProduct.getColorId());
        Size size = sizeService.findById(newVariantProduct.getSizeId());
        if (updateVariantProduct != null) {
            updateVariantProduct.setProduct(product);
            updateVariantProduct.setColor(color);
            updateVariantProduct.setSize(size);
            updateVariantProduct.setQuantity(newVariantProduct.getQuantity());
            updateVariantProduct = variantProductService.saveOrUpdate(updateVariantProduct);
        } else {
            newVariantProduct.setProduct(product);
            newVariantProduct.setSize(size);
            newVariantProduct.setColor(color);
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            VariantProduct variantProductRequest = modelMapper.map(newVariantProduct, VariantProduct.class);
            variantProductRequest.setId(id);
            updateVariantProduct = variantProductService.saveOrUpdate(updateVariantProduct);
        }
        VariantProductViewDTO variantProductResponse = modelMapper.map(updateVariantProduct, VariantProductViewDTO.class);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update variant product successfully", variantProductResponse));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Result> deleteVariantProduct(@PathVariable Integer id) {
        boolean exists = variantProductService.existsById(id);
        if (exists) {
            variantProductService.delete(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Delete variant product successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Result(404, "Cannot find variant product to delete", ""));
    }
}
