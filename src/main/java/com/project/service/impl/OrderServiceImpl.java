package com.project.service.impl;

import com.project.dto.request.OrderDTO;
import com.project.dto.request.UserDTO;
import com.project.exception.base.CustomException;
import com.project.model.*;
import com.project.repository.CartRepository;
import com.project.repository.OrderDetailRepository;
import com.project.repository.OrderRepository;
import com.project.service.OrderService;
import com.project.service.ProductService;
import com.project.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    public List<OrderDTO> getAll() {
        return this.orderRepository.findAll().stream()
                .map(o -> modelMapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderDTO> getAll(String keyword, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Order> orders = this.orderRepository.findAll(pageable);
        if (!orders.isEmpty()) {
            List<OrderDTO> orderDtoList = orders.getContent()
                    .stream()
                    .map(o -> modelMapper.map(o, OrderDTO.class))
                    .collect(Collectors.toList());

            return new PageImpl<>(orderDtoList, pageable, orders.getTotalElements());
        }
        return null;
    }

    @Override
    public OrderDTO findById(Integer id) {
        Order order = this.orderRepository.findById(id).orElseThrow(()->new CustomException.NotFoundException("Order not found with id : " + id, 404, new Date()));
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public List<OrderDTO> findByName(String name) {
        return null;
    }

    @Override
    @Transactional
    public OrderDTO save(OrderDTO orderDto) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO user = userService.findByEmail(customUserDetails.getEmail());
        orderDto.setUser(modelMapper.map(user, User.class));
        orderDto.setOrderCode(this.generateCode());
        orderDto.setStatus(false);
        Date localDate = new Date();
        orderDto.setOrderDate(localDate);
        Order orderRequest = modelMapper.map(orderDto, Order.class);
        Order order = this.orderRepository.save(orderRequest);
        List<Cart> carts = this.cartRepository.getCheckout(user.getId());
        for (Cart cart: carts) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setPrice(cart.getProduct().getPrice());
            orderDetail.setQuantity(cart.getQuantity());
            orderDetail.setSubtotal(cart.getProduct().getPrice() * cart.getQuantity());
            orderDetail.setOrder(order);
            orderDetail.setProduct(cart.getProduct());
            this.orderDetailRepository.save(orderDetail);
        }
        this.cartRepository.deleteAll(carts);
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public OrderDTO update(OrderDTO orderDto, Integer id) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO user = userService.findByEmail(customUserDetails.getEmail());
        Order order = modelMapper.map(this.findById(id), Order.class);
        order.setStatus(orderDto.getStatus());
        order.setTotal(orderDto.getTotal());
        order.setShippingMethod(orderDto.getShippingMethod());
        order.setPaymentMethod(orderDto.getPaymentMethod());
        order.setOrderNote(orderDto.getOrderNote());
        order.setOrderCode(orderDto.getOrderCode());
        order.setOrderDate(orderDto.getOrderDate());
        order.setUser(modelMapper.map(user, User.class));
        order = this.orderRepository.save(order);
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public void delete(Integer id, boolean check) {
        Order order = this.orderRepository.findById(id).orElseThrow(()->new CustomException.NotFoundException("Order not found with id : " + id, 404, new Date()));
        List<OrderDetail> list = this.orderDetailRepository.checkInOrderDetail(id);
        if(!list.isEmpty()){
            if(!check){
                throw new CustomException.NotImplementedException("Contains foreign keys that cannot be deleted", 501, new Date());
            } else {
                this.orderDetailRepository.deleteAll(list);
            }
        } else {
            this.orderRepository.delete(order);
        }
    }

    private String generateCode(){
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 15; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }
        return  sb.toString();
    }

}
