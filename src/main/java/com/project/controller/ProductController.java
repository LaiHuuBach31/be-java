package com.project.controller;

import com.project.dto.ProductDTO;
import com.project.dto.ProductViewDTO;
import com.project.model.Category;
import com.project.model.Item;
import com.project.model.Product;
import com.project.model.Result;
import com.project.service.CategoryService;
import com.project.service.ItemService;
import com.project.service.ProductService;
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
@RequestMapping(value = "/api/v1/product")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<Result> getAllProduct(@Param("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Page<Product> listProduct = productService.pagination(pageNo, 2);
        if(keyword != null){
            listProduct = this.productService.search(keyword, pageNo, 2);
        }
        if (!listProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Query list product successfully", productService));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(404, "No products found", null));
        }
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Result> getById(@PathVariable Integer id) {
        Product product = productService.findById(id);
        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Query category successfully", product));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(404, "Cannot find category", null));
        }
    }

    @PostMapping()
    public ResponseEntity<Result> insertProduct(@Valid @RequestBody ProductDTO newProduct,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> errorMessages = errors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Result(400, "Validation failed", errorMessages));
        }
        List<Product> foundProduct = productService.findByName(newProduct.getName().trim());
        if (!foundProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new Result(501, "Product name already taken", ""));
        }
        Category category = categoryService.findById(newProduct.getCategoryId());
        Item item = itemService.findById(newProduct.getItemId());
        newProduct.setCategory(category);
        newProduct.setItem(item);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        Product productRequest = modelMapper.map(newProduct, Product.class);
        Product product = productService.saveOrUpdate(productRequest);
        ProductViewDTO productResponse = modelMapper.map(product, ProductViewDTO.class);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Insert product successfully", productResponse));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Result> updateProduct(@Valid @RequestBody ProductDTO newProduct,
                                                 BindingResult bindingResult, @PathVariable Integer id) {
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            List<String> errorMessages = errors.stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Result(400, "Validation failed", errorMessages));
        }
        List<Product> foundProduct = productService.findByName(newProduct.getName().trim());
        if (!foundProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new Result(501, "Product name already taken", ""));
        }
        Product updateProduct = productService.findById(id);
        Category category = categoryService.findById(newProduct.getCategoryId());
        Item item = itemService.findById(newProduct.getItemId());
        if (updateProduct != null) {
            updateProduct.setName(newProduct.getName());
            updateProduct.setPrice(newProduct.getPrice());
            updateProduct.setDiscount(newProduct.getDiscount());
            updateProduct.setImage(newProduct.getImage());
            updateProduct.setCategory(category);
            updateProduct.setItem(item);
            updateProduct.setDescription(newProduct.getDescription());
            updateProduct.setStatus(newProduct.getStatus());
            updateProduct = productService.saveOrUpdate(updateProduct);
        } else {
            newProduct.setCategory(category);
            newProduct.setItem(item);
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            Product productRequest = modelMapper.map(newProduct, Product.class);
            productRequest.setId(id);
            updateProduct = productService.saveOrUpdate(productRequest);
        }
        ProductViewDTO productResponse = modelMapper.map(updateProduct, ProductViewDTO.class);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update product successfully", productResponse));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Result> deleteCategory(@PathVariable Integer id) {
        boolean exists = productService.existsById(id);
        if (exists) {
            productService.delete(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Delete product successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Result(404, "Cannot find product to delete", ""));
    }
}
