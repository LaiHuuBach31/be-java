package com.project.model;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class Token extends GenericEntity{
    @Column(name="token", unique = true)
    public String token;
    @Column(name="tokentype", columnDefinition = "BEARER")
    public String tokenType;
    @Column(name="revoked")
    public boolean revoked;
    @Column(name="expired")
    public boolean expired;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    public User user;
}
