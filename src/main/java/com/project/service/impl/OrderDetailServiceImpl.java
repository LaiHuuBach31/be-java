package com.project.service.impl;

import com.project.dto.request.OrderDetailDTO;
import com.project.dto.response.OrderDetailViewDTO;
import com.project.exception.base.CustomException;
import com.project.model.*;
import com.project.repository.OrderDetailRepository;
import com.project.service.OrderDetailService;
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
public class OrderDetailServiceImpl implements OrderDetailService {

    private final ModelMapper modelMapper;
    private final OrderDetailRepository orderDetailRepository;


    @Override
    public List<OrderDetailViewDTO> getAll() {
        return this.orderDetailRepository.findAll().stream()
                .map(o -> modelMapper.map(o, OrderDetailViewDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderDetailViewDTO> getAll(String keyword, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);;
        Page<OrderDetail> orderDetails = this.orderDetailRepository.findAll(pageable);
        if (!orderDetails.isEmpty()) {
            List<OrderDetailViewDTO> orderDetailDtoList = orderDetails.getContent()
                    .stream()
                    .map(o -> modelMapper.map(o, OrderDetailViewDTO.class))
                    .collect(Collectors.toList());

            return new PageImpl<>(orderDetailDtoList, pageable, orderDetails.getTotalElements());
        }
        return null;
    }

    @Override
    public OrderDetailViewDTO findById(Integer id) {
        OrderDetail orderDetail = this.orderDetailRepository.findById(id).orElseThrow(()->new CustomException.NotFoundException("OrderDetail not found with id : " + id, 404, new Date()));
        return modelMapper.map(orderDetail, OrderDetailViewDTO.class);
    }

    @Override
    public List<OrderDetailViewDTO> findByName(String name) {
        return null;
    }

    @Override
    public OrderDetailViewDTO save(OrderDetailDTO orderDetailDto) {
        return null;
    }

    @Override
    public OrderDetailViewDTO update(OrderDetailDTO orderDetailDto, Integer id) {
        return null;
    }

    @Override
    public void delete(Integer id, boolean check) {
        OrderDetail orderDetail = this.orderDetailRepository.findById(id).orElseThrow(()->new CustomException.NotFoundException("OrderDetail not found with id : " + id, 404, new Date()));
        this.orderDetailRepository.delete(orderDetail);
    }

}
