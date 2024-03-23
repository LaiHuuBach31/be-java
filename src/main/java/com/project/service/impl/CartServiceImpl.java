package com.project.service.impl;

import com.project.dto.request.CartDTO;
import com.project.dto.request.ColorDTO;
import com.project.dto.request.SizeDTO;
import com.project.dto.request.UserDTO;
import com.project.dto.response.CartViewDTO;
import com.project.dto.response.ProductViewDTO;
import com.project.exception.base.CustomException;
import com.project.model.*;
import com.project.repository.CartRepository;
import com.project.service.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final ProductService productService;
    private final SizeService sizeService;
    private final ColorService colorService;
    private final ModelMapper modelMapper;

    @Override
    public List<CartViewDTO> getAll(){
        List<Cart> carts = this.cartRepository.findAll();
        return carts.stream().map(e -> modelMapper.map(e, CartViewDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Page<CartViewDTO> getAll(String keyword, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        Page<Cart> carts = this.cartRepository.findAll(pageable);
        List<CartViewDTO> cartList = carts.getContent()
                .stream()
                .map(e -> modelMapper.map(e, CartViewDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(cartList, pageable, carts.getTotalElements());
    }

    @Override
    public CartViewDTO findById(Integer id) {
        Cart cart = this.cartRepository.findById(id).orElseThrow(()->new CustomException.NotFoundException("Cart not found with id : " + id, 404, new Date()));
        return modelMapper.map(cart, CartViewDTO.class);
    }

    @Override
    public List<CartViewDTO> findByName(String name) {return null;}

    @Override
    public CartViewDTO save(CartDTO cartDto) {
        List<Cart> carts = this.cartRepository.findAll();
        if(carts.isEmpty()){
            return addNewCart(cartDto);
        } else {
            for (Cart cart : carts) {
                Cart cartCheck = this.cartRepository.checkCart(cartDto.getProductId(), cartDto.getColorId(), cartDto.getSizeId());
                if(cartCheck != null){
                    cart.setQuantity(cartDto.getQuantity() + cart.getQuantity());
                    this.cartRepository.save(cart);
                    return modelMapper.map(cart, CartViewDTO.class);
                }
                return addNewCart(cartDto);
            }
        }
        return null;
    }

    @Override
    public CartViewDTO update(CartDTO cartDto, Integer id) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO user = userService.findByEmail(customUserDetails.getEmail());
        ProductViewDTO product = productService.findById(cartDto.getProductId());
        SizeDTO size = sizeService.findById(cartDto.getSizeId());
        ColorDTO color = colorService.findById(cartDto.getColorId());
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        Cart cart = modelMapper.map(this.findById(id), Cart.class);
        cart.setUser(modelMapper.map(user, User.class));
        cart.setProduct(modelMapper.map(product, Product.class));
        cart.setColor(modelMapper.map(color, Color.class));
        cart.setSize(modelMapper.map(size, Size.class));
        cart.setQuantity(cartDto.getQuantity());
        cart = this.cartRepository.save(cart);
        return modelMapper.map(cart, CartViewDTO.class);
    }

    @Override
    public void delete(Integer id, boolean check) {
        Cart cart = this.cartRepository.findById(id).orElseThrow(()-> new CustomException.NotFoundException("Cart item not found with id : " + id, 404, new Date()));
        this.cartRepository.delete(cart);
    }

    @Override
    public CartViewDTO update(Integer id, Integer quantity, Integer sizeId, Integer colorId) {
        CartViewDTO c = this.findById(id);
        Cart cart = null;
        if(quantity != null){
            c.setQuantity(quantity);
            cart = this.cartRepository.save(modelMapper.map(c, Cart.class));
        }
        if(colorId != null){
            c.setColor(modelMapper.map(colorService.findById(colorId) ,Color.class));
            cart = this.cartRepository.save(modelMapper.map(c, Cart.class));
        }
        if(sizeId != null){
            c.setSize(modelMapper.map(sizeService.findById(sizeId) ,Size.class));
            cart = this.cartRepository.save(modelMapper.map(c, Cart.class));
        }
        return modelMapper.map(cart, CartViewDTO.class);
    }

    private CartViewDTO addNewCart(CartDTO cartItem) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO user = userService.findByEmail(customUserDetails.getEmail());
        ProductViewDTO product = productService.findById(cartItem.getProductId());
        SizeDTO size = sizeService.findById(cartItem.getSizeId());
        ColorDTO color = colorService.findById(cartItem.getColorId());
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        cartItem.setUser(modelMapper.map(user, User.class));
        cartItem.setProduct(modelMapper.map(product, Product.class));
        cartItem.setSize(modelMapper.map(size, Size.class));
        cartItem.setColor(modelMapper.map(color, Color.class));
        Cart cartRequest = modelMapper.map(cartItem, Cart.class);
        Cart c = cartRepository.save(cartRequest);
        return modelMapper.map(c, CartViewDTO.class);
    }

}
