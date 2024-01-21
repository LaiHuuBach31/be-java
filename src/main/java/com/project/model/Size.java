package com.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sizes")
public class Size extends GenericEntity{
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @Column(name = "status", nullable = false)
    private Integer status;
    @OneToMany(mappedBy = "size", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<VariantProduct> sizeAttr;
//    @OneToMany(mappedBy = "size")
//    @JsonIgnore
//    private Set<CartVariant> sizes;
    @OneToMany(mappedBy = "size")
    @JsonIgnore
    private Set<Cart> sizes;
}
