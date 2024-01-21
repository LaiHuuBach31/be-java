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
@Table(name = "colors")
public class Color extends GenericEntity{
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @Column(name = "status", nullable = false)
    private Integer status;
    @OneToMany(mappedBy = "color", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<VariantProduct> colorAttr;
//    @OneToMany(mappedBy = "color")
//    @JsonIgnore
//    private Set<CartVariant> colors;
    @OneToMany(mappedBy = "color")
    @JsonIgnore
    private Set<Cart> colors;
}
