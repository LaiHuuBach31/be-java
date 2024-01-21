package com.project.controller;

import com.project.dto.CartDTO;
import com.project.dto.CartViewDTO;
import com.project.model.*;
import com.project.service.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;
//    private final CartVariantService cartVariantService;
    private final SizeService sizeService;
    private final ColorService colorService;
    private final ModelMapper modelMapper;

    @GetMapping(value = "")
    public ResponseEntity<Result> getAllCart() {
        List<Cart> listCart = cartService.getAll();
        if (!listCart.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Query list cart successfully", listCart));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(404, "No cart found", null));
        }
    }

//    @PostMapping
//    public ResponseEntity<Result> addCart(@RequestBody CartDTO cartItem){
//        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Optional<User> userOptional = userService.findByEmail(customUserDetails.getEmail());
//        User user = userOptional.orElse(null);
//        cartItem.setUser(user);
//        Product product = productService.findById(cartItem.getProductId());
//        cartItem.setProduct(product);
//        modelMapper.getConfiguration().setAmbiguityIgnored(true);
//        Cart cartRequest = modelMapper.map(cartItem, Cart.class);
//        Cart cart = cartService.saveOrUpdate(cartRequest);
//        CartViewDTO cartResponse = modelMapper.map(cart, CartViewDTO.class);
//        Size size = sizeService.findById(cartItem.getSizeId());
//        Color color = colorService.findById(cartItem.getColorId());
//        if(cart!=null && size!=null && color!=null){
//            CartVariant cartVariant = new CartVariant();
//            cartVariant.setCart(cart);
//            cartVariant.setSize(size);
//            cartVariant.setColor(color);
//            cartVariant = cartVariantService.saveOrUpdate(cartVariant);
//            if(cartVariant!=null){
//                return ResponseEntity.status(HttpStatus.OK)
//                        .body(new Result(200, "Add to cart successfully", cartResponse));
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(new Result(404, "Add to cart failed" , null));
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new Result(404, "Add to cart failed" , null));
//        }
//    }

    @PostMapping
    public ResponseEntity<Result> addCart(@RequestBody CartDTO cartItem) {
        List<Cart> carts = cartService.getAll();
        if(carts.isEmpty()){
            return addNewCart(cartItem);
        } else {
            for (Cart cart : carts) {

                Cart cartCheck = cartService.checkCart(cartItem.getProductId(), cartItem.getColorId(), cartItem.getSizeId());

                if(cartCheck != null){
                    return updateExistingCart(cartCheck, cartItem.getQuantity());
                } else {
                    return addNewCart(cartItem);
                }
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new Result(404, "Add to cart failed", null));
    }

//    @PutMapping
//    public ResponseEntity<Result> updateCart(@RequestBody CartDTO cartItem){
//        return null;
//    }
//
//    @DeleteMapping
//    public ResponseEntity<Result> deleteCart(@PathVariable Integer id){
//        boolean exit = cartService.existsById(id);
//        if(exit){
//            cartService.delete(id);
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(new Result(200, "Delete cart item successfully", ""));
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(new Result(404, "Cannot find cart item to delete", ""));
//    }

    private ResponseEntity<Result> addNewCart(CartDTO cartItem) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByEmail(customUserDetails.getEmail()).orElse(null);
        cartItem.setUser(user);
        Product product = productService.findById(cartItem.getProductId());
        cartItem.setProduct(product);
        Size size = sizeService.findById(cartItem.getSizeId());
        cartItem.setSize(size);
        Color color = colorService.findById(cartItem.getColorId());
        cartItem.setColor(color);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        Cart cartRequest = modelMapper.map(cartItem, Cart.class);
        Cart c = cartService.saveOrUpdate(cartRequest);
        CartViewDTO cartResponse = modelMapper.map(c, CartViewDTO.class);

        if (cartResponse != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new Result(200, "Add to cart successfully", cartResponse));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(404, "Add to cart failed", null));
        }
    }

    private ResponseEntity<Result> updateExistingCart(Cart cart, int quantity) {
        cart.setQuantity(quantity + cart.getQuantity());
        cartService.saveOrUpdate(cart);
        CartViewDTO cartResponse = modelMapper.map(cart, CartViewDTO.class);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new Result(200, "Add to cart successfully", cartResponse));
    }


}
