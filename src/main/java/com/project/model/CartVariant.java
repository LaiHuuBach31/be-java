//package com.project.model;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.*;
//
//@Data
//@EqualsAndHashCode(callSuper = true)
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "cartvariant")
//public class CartVariant extends GenericEntity {
//    @ManyToOne
//    @JoinColumn(name = "cartId",referencedColumnName = "id")
//    private Cart cart;
//    @ManyToOne
//    @JoinColumn(name = "sizeId",referencedColumnName = "id")
//    private Size size;
//    @ManyToOne
//    @JoinColumn(name = "colorId",referencedColumnName = "id")
//    private Color color;
//}
