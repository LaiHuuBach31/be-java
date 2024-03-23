package com.project.dto.request;

import com.project.model.OrderDetail;
import com.project.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Integer id;
    private int status;
    private int total;
    private String shippingMethod;
    private String paymentMethod;
    private String orderNote;
    private Date orderDate;
    private String orderCode;
    private String token;
    private User user;
    private Set<OrderDetail> orderProduct;
}
