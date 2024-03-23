package com.project.controller;

import com.project.dto.request.CartDTO;
import com.project.dto.response.CartViewDTO;
import com.project.model.*;
import com.project.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<Result> getAllCart() {
        List<CartViewDTO> listCart = cartService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Query list cart successfully", listCart));
    }

    @PostMapping
    public ResponseEntity<Result> addCart(@RequestBody CartDTO cartItem) {
        CartViewDTO cartViewDto = cartService.save(cartItem);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Add to cart successfully", cartViewDto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Result> updateCart(@RequestBody CartDTO cartItem, @PathVariable Integer id){
        CartViewDTO cartViewDto = cartService.update(cartItem, id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update cart successfully", cartViewDto));
    }
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<Result> updateCart(
                                    @Param("quantity") Integer quantity,
                                    @Param("colorId") Integer colorId,
                                    @Param("sizeId") Integer sizeId,
                                    @PathVariable Integer id){
        CartViewDTO cartViewDto = cartService.update(id, quantity, sizeId, colorId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Update cart successfully", cartViewDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Result> deleteCart(@PathVariable Integer id, @RequestParam(name="check", defaultValue = "false") boolean check){
        this.cartService.delete(id, check);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Delete cart successfully", null));
    }


}
