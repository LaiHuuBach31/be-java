package com.project.service;

import com.project.dto.request.OrderDetailDTO;
import com.project.dto.response.OrderDetailViewDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailService extends Generic<OrderDetailDTO, OrderDetailViewDTO>{
}
