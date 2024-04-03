package com.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends GenericEntity{
    @Column(name = "status")
    private Boolean status;
    @Column(name = "total")
    private int total;
    @Column(name = "shippingmethod")
    private String shippingMethod;
    @Column(name = "paymentmethod")
    private String paymentMethod;
    @Column(name = "ordernote")
    private String orderNote;
    @Column(name = "orderdate")
    private Date orderDate;
    @Column(name = "ordercode")
    private String orderCode;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;
//    @OneToMany(mappedBy = "order")
//    private List<OrderDetail> orderProduct;
}
