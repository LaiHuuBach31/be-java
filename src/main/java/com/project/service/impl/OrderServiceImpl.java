package com.project.service.impl;

import com.project.dto.request.OrderDTO;
import com.project.exception.base.CustomException;
import com.project.model.Order;
import com.project.repository.OrderRepository;
import com.project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<OrderDTO> getAll() {
        return this.orderRepository.findAll().stream()
                .map(o -> modelMapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderDTO> getAll(String keyword, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);;
        Page<Order> orders = this.orderRepository.findAll(pageable);
        if (!orders.isEmpty()) {
            List<OrderDTO> categoryDtoList = orders.getContent()
                    .stream()
                    .map(o -> modelMapper.map(o, OrderDTO.class))
                    .collect(Collectors.toList());

            return new PageImpl<>(categoryDtoList, pageable, orders.getTotalElements());
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
    public OrderDTO save(OrderDTO orderDto) {
        Order orderRequest = modelMapper.map(orderDto, Order.class);
        Order order = this.orderRepository.save(orderRequest);
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public OrderDTO update(OrderDTO orderDto, Integer id) {
        OrderDTO o = this.findById(id);
        Order order = modelMapper.map(o, Order.class);
        order.setStatus(orderDto.getStatus());
        order.setTotal(orderDto.getTotal());
        order.setShippingMethod(orderDto.getShippingMethod());
        order.setPaymentMethod(orderDto.getPaymentMethod());
        order.setOrderNote(orderDto.getOrderNote());
        order.setOrderCode(orderDto.getOrderCode());
        order.setOrderDate(orderDto.getOrderDate());
        order.setToken(orderDto.getToken());
        order.setUser(orderDto.getUser());
        order = this.orderRepository.save(order);
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public void delete(Integer id, boolean check) {
        Order order = this.orderRepository.findById(id).orElseThrow(()->new CustomException.NotFoundException("Order not found with id : " + id, 404, new Date()));
        this.orderRepository.delete(order);
    }
}
