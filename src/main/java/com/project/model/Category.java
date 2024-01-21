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
@Table(name = "categories")
public class Category extends GenericEntity {
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @Column(name = "status", nullable = false)
    private int status;
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private Set<Product> products;
}
