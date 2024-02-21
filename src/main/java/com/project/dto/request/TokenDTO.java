package com.project.dto.request;

import com.project.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private Integer id;
    public String token;
    public String tokenType;
    public boolean revoked;
    public boolean expired;
    public User user;
}
